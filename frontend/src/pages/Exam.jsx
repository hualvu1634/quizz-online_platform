import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import examApi from '../api/examApi'; 

const Exam = () => {
  const { examId } = useParams();
  const [examDetail, setExamDetail] = useState(null); 
  const [questions, setQuestions] = useState([]); 
  const [isLoading, setIsLoading] = useState(false); 
  const [userAnswers, setUserAnswers] = useState({}); 
  const [isCompleted, setIsCompleted] = useState(false);
  const [timeLeft, setTimeLeft] = useState(null);
  const [examResult, setExamResult] = useState(null); // Lưu ResultResponse từ Backend

  useEffect(() => { fetchExamDetail(); }, [examId]);

  useEffect(() => {
    if (timeLeft === null || isCompleted) return;
    if (timeLeft <= 0) { handleCompleteExam(); return; }
    const timerId = setInterval(() => { setTimeLeft(prev => prev - 1); }, 1000);
    return () => clearInterval(timerId); 
  }, [timeLeft, isCompleted]);

  const fetchExamDetail = async () => {
    setIsLoading(true);
    try {
      const res = await examApi.getExam(examId);
      const data = res.data || res;
      setExamDetail(data);
      setQuestions(data.questions || []);
      if (data.duration > 0) setTimeLeft(data.duration * 60); 
    } catch (error) { console.error("Lỗi tải bài thi", error); }
    finally { setIsLoading(false); }
  };

  const handleAnswerSelect = (questionId, optionIndex) => {
    if (isCompleted || timeLeft <= 0) return;
    // Chỉ lưu lựa chọn vào state local
    setUserAnswers(prev => ({ ...prev, [questionId]: optionIndex }));
  };

  const handleCompleteExam = async () => {
    try {
      // Chuyển đổi userAnswers sang Map<Long, Integer> cho SubmitRequest
      const answerMap = {};
      Object.keys(userAnswers).forEach(key => {
        answerMap[key] = userAnswers[key];
      });

      const payload = {
        userId: 1, // Nên lấy từ AuthContext/Redux
        examId: parseInt(examId),
        answers: answerMap
      };

      const res = await examApi.submitExam(payload);
      setExamResult(res.data || res); // Lưu ResultResponse (score, correctAnswers...)
      setIsCompleted(true);
    } catch (error) {
      console.error("Lỗi khi nộp bài:", error);
      alert("Nộp bài thất bại!");
    }
  };

  const formatTime = (seconds) => {
    const m = Math.floor(seconds / 60).toString().padStart(2, '0');
    const s = (seconds % 60).toString().padStart(2, '0');
    return `${m}:${s}`;
  };

  if (isLoading) return <div className="text-center mt-5"><div className="spinner-border"></div></div>;

  // GIAO DIỆN KẾT QUẢ SAU KHI NỘP BÀI
  if (isCompleted && examResult) {
    return (
      <div className="container mt-5" style={{ maxWidth: '700px' }}>
        <div className="card shadow border-0 text-center p-5">
          <div className="mb-4">
            <i className="bi bi-patch-check-fill text-success" style={{ fontSize: '5rem' }}></i>
          </div>
          <h2 className="fw-bold text-dark">Kết Quả Bài Thi</h2>
          <p className="text-muted">Chúc mừng bạn đã hoàn thành bài kiểm tra!</p>
          
          <div className="row mt-4">
            <div className="col-6 border-end">
              <h1 className="display-4 fw-bold text-primary">{examResult.score}</h1>
              <p className="text-uppercase small fw-bold">Điểm số</p>
            </div>
            <div className="col-6">
              <h1 className="display-4 fw-bold text-success">{examResult.correctAnswers}/{questions.length}</h1>
              <p className="text-uppercase small fw-bold">Số câu đúng</p>
            </div>
          </div>

          <div className="mt-4 p-3 bg-light rounded">
              <small className="text-muted">Thời gian nộp: {new Date(examResult.submittedAt).toLocaleString()}</small>
          </div>

          <button className="btn btn-primary btn-lg mt-5 px-5 w-100" onClick={() => window.location.href = '/'}>
            Quay về trang chủ
          </button>
        </div>
      </div>
    );
  }

  return (
    <div style={{ backgroundColor: '#f4f4f4', minHeight: '100vh', padding: '40px 0' }}>
        {/* Thanh đếm ngược */}
        {!isCompleted && (
            <div className="sticky-top d-flex justify-content-center mb-4" style={{ top: '20px', zIndex: 1000 }}>
                <div className={`shadow rounded-pill px-4 py-2 text-white fw-bold fs-4 ${timeLeft <= 60 ? 'bg-danger' : 'bg-dark'}`}>
                    {formatTime(timeLeft)}
                </div>
            </div>
        )}

        <div className="container" style={{ maxWidth: '800px' }}>
            <div className="bg-white p-4 rounded shadow-sm mb-4">
                <h2 className="fw-bold">{examDetail?.name}</h2>
                <span className="text-muted">Tổng số câu hỏi: {questions.length}</span>
            </div>

            {questions.map((q, index) => (
                <div key={q.id} className="card border-0 shadow-sm mb-4 p-4">
                    <p className="fw-bold fs-5">Câu {index + 1}: {q.question}</p>
                    <div className="d-flex flex-column gap-2">
                        {q.options.map((opt, optIndex) => (
                            <button
                                key={optIndex}
                                className={`btn text-start p-3 ${userAnswers[q.id] === optIndex ? 'btn-primary text-white' : 'btn-outline-secondary'}`}
                                onClick={() => handleAnswerSelect(q.id, optIndex)}
                            >
                                <span className="me-3 fw-bold">{String.fromCharCode(65 + optIndex)}.</span> {opt}
                            </button>
                        ))}
                    </div>
                </div>
            ))}

            <div className="text-center mt-5">
                <button 
                    className="btn btn-success btn-lg px-5 fw-bold shadow"
                    onClick={handleCompleteExam}
                    disabled={Object.keys(userAnswers).length === 0}
                >
                    Nộp bài ngay
                </button>
            </div>
        </div>
    </div>
  );
};

export default Exam;