import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import categoryApi from '../api/categoryApi';
// Import file api mới
import examApi from '../api/examApi'; 

const Home = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [exams, setExams] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  const queryParams = new URLSearchParams(location.search);
  const activeCategory = queryParams.get('category');

  useEffect(() => {
    fetchData();
  }, [activeCategory]);

  const fetchData = async () => {
    setLoading(true);
    try {
      let resExams;
      if (activeCategory) {
        // Lấy bài thi theo danh mục
        resExams = await categoryApi.getExamsByCategory(activeCategory, 1);
        setExams(resExams.data?.data || []); 
      } else {
        // SỬA LỖI 3: Lấy toàn bộ bài thi khi chọn "Tất cả" (Cần đảm bảo file api có hàm getAll())
        resExams = await examApi.getAll(1); // Lấy page 1 của toàn bộ bài thi
        setExams(resExams.data?.data || []); 
      }
    } catch (error) {
      console.error("Lỗi khi tải dữ liệu:", error);
      setExams([]); 
    } finally {
      setLoading(false);
    }
  };

  const filteredExams = exams.filter(exam => 
    exam.name?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleEnrollClick = (examId) => {
    const isAuthenticated = localStorage.getItem('token'); 

    if (!isAuthenticated) {
      navigate('/login');
    } else {
      // Chuyển hướng sang trang làm bài thi
      navigate(`/exam/${examId}`);
    }
  };

  return (
    <div style={{ backgroundColor: '#f8f9fa', minHeight: '100vh', paddingBottom: '40px' }}>
      <div className="container mt-4">
        <div className="row mb-4 mt-3">
          <div className="col-md-12">
            <input 
              type="text" 
              className="form-control p-3 shadow-sm" 
              placeholder="Tìm kiếm bài kiểm tra..." 
              style={{ borderRadius: '10px' }} 
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>

        {loading ? (
          <div className="text-center py-5">
            <div className="spinner-border text-primary"></div>
          </div>
        ) : (
          <div className="row g-4">
            {filteredExams.length > 0 ? filteredExams.map(exam => {
              return (
                <div className="col-12 col-sm-6 col-md-4 col-lg-3" key={exam.id}>
                  <div className="card h-100 border-0 shadow-sm" style={{ borderRadius: '15px', overflow: 'hidden' }}>
                    <img 
                        src={exam.posterUrl || 'https://via.placeholder.com/300x160'} 
                        className="card-img-top" 
                        alt={exam.name} 
                        style={{ height: '160px', objectFit: 'cover' }} 
                    />
                    <div className="card-body d-flex flex-column">
                      <h6 className="card-title fw-bold">{exam.name}</h6>
                      <p className="text-muted small mb-3">
                        Thời gian: {exam.duration} phút
                      </p>
                      
                      <div className="mt-auto">
                          <button 
                            className="btn btn-primary px-4 w-100"
                            onClick={() => handleEnrollClick(exam.id)}
                          >
                            Làm bài 
                          </button>
                      </div>
                    </div>
                  </div>
                </div>
              );
            }) : (
              <div className="text-center py-5 text-muted w-100">Không tìm thấy bài kiểm tra nào.</div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;