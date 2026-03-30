import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import examApi from '../api/examApi'; 

const Exam = () => {
  const { examId } = useParams();
  const navigate = useNavigate();
  
  const [examDetail, setExamDetail] = useState(null); 
  const [questions, setQuestions] = useState([]); 
  const [isLoading, setIsLoading] = useState(false); 
  const [userAnswers, setUserAnswers] = useState({}); 
  const [isCompleted, setIsCompleted] = useState(false);
  const [timeLeft, setTimeLeft] = useState(null);

  useEffect(() => {
    fetchExamDetail();
  }, [examId]);

  useEffect(() => {
    if (timeLeft === null || isCompleted) return;
    if (timeLeft <= 0) {
      handleCompleteExam(); 
      return;
    }
    const timerId = setInterval(() => {
      setTimeLeft(prev => prev - 1);
    }, 1000);
    return () => clearInterval(timerId); 
  }, [timeLeft, isCompleted]);

  const fetchExamDetail = async () => {
    setIsLoading(true);
    try {
      const res = await examApi.getExam(examId);
      const data = res.data || res; 
      
      setExamDetail(data);
      setQuestions(data.questions || []);
      setUserAnswers({}); 
      setIsCompleted(false); 
      
      if (data.duration > 0) {
        setTimeLeft(data.duration * 60); 
      } else {
        setTimeLeft(0);
      }
    } catch (error) {
      console.error(`Lỗi tải chi tiết bài thi`, error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleAnswerSelect = async (questionId, optionIndex) => {
    if (userAnswers[questionId] || isCompleted || timeLeft <= 0) return;

    try {
      const payload = {
        questionId: questionId,
        selectedOption: optionIndex
      };

      const res = await examApi.checkAnswer(payload);
      const result = res.data || res; 

      // SỬA LỖI ĐÁP ÁN: Lấy cả result.correct (Lombok JSON) hoặc result.isCorrect
      const isTrue = result.correct !== undefined ? result.correct : result.isCorrect;

      setUserAnswers(prev => ({
        ...prev,
        [questionId]: {
          selectedOption: optionIndex,
          isCorrect: isTrue,
          correctAnswer: result.correctAnswer 
        }
      }));
    } catch (error) {
      console.error("Lỗi khi kiểm tra đáp án:", error);
    }
  };

  const formatTime = (seconds) => {
    if (seconds === null) return "00:00";
    const m = Math.floor(seconds / 60).toString().padStart(2, '0');
    const s = (seconds % 60).toString().padStart(2, '0');
    return `${m}:${s}`;
  };

  const handleResetExam = () => {
    setUserAnswers({});
    setIsCompleted(false);
    if (examDetail && examDetail.duration > 0) {
        setTimeLeft(examDetail.duration * 60); 
    }
  };

  const isAllCorrect = questions.length === 0 || questions.every(q => {
    const answer = userAnswers[q.id];
    return answer && answer.isCorrect === true;
  });

  const handleCompleteExam = async () => {
    try {
      const payload = {
        userId: 1, 
        examId: examDetail?.id || examId
      };
      await examApi.completeExam(payload);
      setIsCompleted(true);
    } catch (error) {
      console.error("Lỗi khi hoàn thành bài thi:", error);
      alert("Có lỗi xảy ra khi lưu tiến độ!");
    }
  };

  if (isLoading) return <div className="text-center mt-5"><div className="spinner-border text-primary"></div></div>;
  if (!examDetail) return <div className="text-center mt-5 text-danger">Không tìm thấy bài thi!</div>;

  return (
    <div style={{ backgroundColor: '#f4f4f4', minHeight: '100vh', padding: '40px 0' }}>
        
        {!isCompleted && timeLeft > 0 && (
            <div className="sticky-top d-flex justify-content-center mb-4" style={{ top: '20px', zIndex: 1000 }}>
                <div className={`shadow rounded-pill px-4 py-2 text-white fw-bold fs-4 ${timeLeft <= 60 ? 'bg-danger blink-bg' : 'bg-dark'}`} 
                     style={{ border: '3px solid white', transition: 'background-color 0.3s' }}>
                    <i className="bi bi-stopwatch me-2"></i> 
                    {formatTime(timeLeft)}
                </div>
            </div>
        )}

        <div className="container" style={{ maxWidth: '900px' }}>
            <div className="bg-white p-4 rounded shadow-sm mb-4 border-top border-4 border-primary">
                <h2 className="fw-bold mb-3">{examDetail.name}</h2>
                <div className="d-flex gap-4 text-muted">
                    <span><i className="bi bi-clock me-1"></i> Tổng thời gian: {examDetail.duration} phút</span>
                  
                </div>
            </div>

            {questions.length > 0 ? (
                <div className="bg-white p-4 rounded shadow-sm">
                <div className="mb-4 d-flex justify-content-between align-items-center">
                    <h4 className="fw-bold mb-0">Danh sách câu hỏi</h4>
                    <span className="badge bg-secondary fs-6">{questions.length} câu</span>
                </div>
                
                {questions.map((q, index) => {
                    const answerData = userAnswers[q.id];
                    const isAnswered = !!answerData;

                    return (
                    <div key={q.id} className="mb-4 p-4 bg-white rounded shadow-sm border border-light">
                        <p className="fw-semibold mb-3 fs-5">Câu {index + 1}: {q.question}</p>
                        
                        <div className="d-flex flex-column gap-2">
                        {q.options.map((opt, optIndex) => {
                            // SỬA LỖI MÀU XÁM: Trả lại đúng class gốc của bác
                            let btnClass = 'btn-light text-dark border-0 hover-shadow';
                            
                            if (isAnswered) {
                                if (answerData.correctAnswer === optIndex) {
                                    btnClass = 'btn-success text-white border-0'; 
                                } else if (answerData.selectedOption === optIndex && !answerData.isCorrect) {
                                    btnClass = 'btn-danger text-white border-0'; 
                                } else {
                                    btnClass = 'btn-light text-dark border-0'; // Đáp án thường
                                }
                            }

                            return (
                            <button
                                key={optIndex}
                                className={`btn text-start ${btnClass}`} 
                                onClick={() => handleAnswerSelect(q.id, optIndex)}
                                disabled={isAnswered || isCompleted || timeLeft <= 0}
                                style={{ transition: 'all 0.2s', padding: '12px 20px' }}
                            >
                                <span className="fw-bold me-3 border-end border-secondary pe-3">
                                    {String.fromCharCode(65 + optIndex)}
                                </span> 
                                {opt}
                            </button>
                            );
                        })}
                        </div>

                        {isAnswered && (
                        <div className="mt-3">
                            {answerData.isCorrect ? (
                            <p className="text-success fw-bold mb-0"><i className="bi bi-check-circle me-1"></i> Câu trả lời của bạn chính xác!</p>
                            ) : (
                            <p className="text-danger fw-bold mb-0"><i className="bi bi-x-circle me-1"></i> Câu trả lời của bạn chưa chính xác!</p>
                            )}
                        </div>
                        )}
                    </div>
                    );
                })}

                <div className="d-flex flex-column align-items-center mt-5 pt-4 border-top">
                    {!isCompleted ? (
                        <>
                            <button 
                                className={`btn btn-lg px-5 fw-bold shadow-sm ${isAllCorrect ? 'btn-success' : 'btn-secondary'}`} 
                                onClick={handleCompleteExam}
                                disabled={!isAllCorrect} 
                            >
                                <i className="bi bi-cloud-arrow-up-fill me-2"></i> Nộp bài
                            </button>
                            
                            {!isAllCorrect && (
                                <small className="text-danger mt-3 fw-medium">
                                    * Bạn cần trả lời đúng tất cả câu hỏi để hoàn thành bài thi.
                                </small>
                            )}
                        </>
                    ) : (
                        <div className="alert alert-success fw-bold fs-5 px-5 py-3 text-center w-100">
                            <i className="bi bi-trophy-fill me-2 fs-4"></i> Xin chúc mừng! Bạn đã hoàn thành bài kiểm tra.
                        </div>
                    )}
                    
                    <button className="btn btn-link text-decoration-none mt-3" onClick={handleResetExam}>
                        <i className="bi bi-arrow-clockwise me-1"></i> Làm lại từ đầu
                    </button>
                </div>
                </div>
            ) : (
                <div className="bg-white p-5 rounded shadow-sm text-center text-muted">
                    <i className="bi bi-inbox fs-1"></i>
                    <p className="mt-2 fs-5">Bài thi này chưa có câu hỏi nào.</p>
                </div>
            )}
        </div>
        <style dangerouslySetInnerHTML={{__html: `
            @keyframes blink { 0% { background-color: #dc3545; } 50% { background-color: #ffc107; color: black; } 100% { background-color: #dc3545; } }
            .blink-bg { animation: blink 1s infinite; }
            .hover-shadow:hover { box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        `}} />
    </div>
  );
};

export default Exam;