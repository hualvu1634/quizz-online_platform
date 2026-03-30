import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import authApi from '../api/authApi';


const SignUp = () => {
  const [formData, setFormData] = useState({
    name: '',       
    email: '',
    password: '',
    phoneNumber: '',  

  });

  
  const navigate = useNavigate();


  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSignUp = async (e) => {
    e.preventDefault();
    try {
      await authApi.register(formData); // Gọi API đăng ký
      navigate('/login'); 
    } catch (err) {
      alert(err.response?.data?.message || "Đăng ký thất bại");
    }
  };

    return (
    <div className="container mt-5 mb-5">
      <div className="mx-auto bg-white p-4" style={{maxWidth: '450px'}}>
        <h3 className="mb-4 fw-bold">Đăng ký tài khoản</h3>
        <form onSubmit={handleSignUp}>
    
           <div className="mb-3"><label className="form-label text-muted">Họ và tên</label><input type="text" name="name" className="form-control rounded-0"  onChange={handleChange} required /></div>
           <div className="mb-3"><label className="form-label text-muted">Số điện thoại</label><input type="text" name="phoneNumber" className="form-control rounded-0" onChange={handleChange} required /></div>
           <div className="mb-3"><label className="form-label text-muted">Email</label><input type="text" name="email" className="form-control rounded-0" onChange={handleChange} required /></div>
           <div className="mb-3"><label className="form-label text-muted">Mật khẩu</label><input type="password" name="password" className="form-control rounded-0" onChange={handleChange} required /></div>
          <button type="submit" className="btn btn-danger w-100 rounded-0 py-2 fw-bold mb-4">ĐĂNG KÝ</button>
        </form>
        <div className="text-center mt-2">
          <span className="text-muted">Đã có tài khoản? </span>
          <Link to="/login" className="text-danger text-decoration-none fw-bold">Đăng nhập</Link>
        </div>
      </div>
    </div>
  );
};

export default SignUp;