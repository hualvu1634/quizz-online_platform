import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import categoryApi from '../api/categoryApi'; 

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [categories, setCategories] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);
  const [userName, setUserName] = useState('Người dùng');

  const queryParams = new URLSearchParams(location.search);
  const activeCategory = queryParams.get('category');

  useEffect(() => {
    const token = localStorage.getItem('token');
    setIsLoggedIn(!!token);
    
    if (localStorage.getItem('email')) {
      setUserName(localStorage.getItem('email'));
    }
  }, [location]); 

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const res = await categoryApi.getAll(); 
        setCategories(res.data || []);
      } catch (error) {
        console.error("Lỗi lấy danh mục:", error);
      }
    };
    fetchCategories();
  }, []);
     

  const handleLogout = () => {
  
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      localStorage.removeItem('email');
      setIsLoggedIn(false);
      setShowDropdown(false);
      navigate('/login');
    
  };

  const handleCategoryClick = (categoryId) => {
    if (categoryId) {
      navigate(`/?category=${categoryId}`);
    } else {
      navigate(`/`);
    }
  };

  const getTabStyle = (id) => {
    const isActive = activeCategory === (id ? id.toString() : null);
    return {
      background: 'none',
      border: 'none',
      padding: '10px 15px',
      cursor: 'pointer',
      fontWeight: isActive ? '700' : '500',
      color: isActive ? '#4441e8' : '#666',
      borderBottom: isActive ? '2px solid #4441e8' : '2px solid transparent',
      whiteSpace: 'nowrap',
      transition: 'all 0.3s'
    };
  };

  return (
    <div style={{ 
      display: 'flex', 
      alignItems: 'center', 
      padding: '0 20px', 
      backgroundColor: '#ffffff', 
      boxShadow: '0 2px 4px rgba(0,0,0,0.05)',
      position: 'sticky',
      top: 0,
      zIndex: 1000
    }}>
      {/* 1. Phần Logo */}
      <div 
        style={{ display: 'flex', alignItems: 'center', cursor: 'pointer', marginRight: '30px' }} 
        onClick={() => navigate('/')}
      >
        <span style={{ fontSize: '22px', fontWeight: '900', color: '#4441e8', letterSpacing: '0.5px' }}>
          HUALVU
        </span>
      </div>

      {/* 2. Phần danh mục (Tabs) ở giữa */}
      <div style={{ 
        display: 'flex', 
        flex: 1, 
        overflowX: 'auto', 
        scrollbarWidth: 'none',
        msOverflowStyle: 'none'
      }}>
        <button 
          style={getTabStyle(null)} 
          onClick={() => handleCategoryClick(null)}
        >
          Tất cả khóa học
        </button>
        {categories.map(cat => (
          <button 
            key={cat.id} 
            style={getTabStyle(cat.id)} 
            onClick={() => handleCategoryClick(cat.id)}
          >
            {cat.name}
          </button>
        ))}
      </div>

      {/* 3. Phần Action (Đăng nhập / Đăng xuất) */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '15px', marginLeft: '20px', padding: '10px 0' }}>
        {isLoggedIn ? (
          <button 
            onClick={handleLogout} 
            style={{ 
              background: 'none', border: '1px solid #ef4444', color: '#ef4444', 
              padding: '6px 16px', borderRadius: '4px', fontWeight: '600', cursor: 'pointer' 
            }}
          >
            Đăng xuất
          </button>
        ) : (
          <>
            <button 
              onClick={() => navigate('/login')} 
              style={{ 
                background: 'none', border: 'none', color: '#4a6ee0', 
                fontWeight: '600', cursor: 'pointer', fontSize: '15px' 
              }}
            >
              Đăng nhập
            </button>
            <button 
              onClick={() => navigate('/signup')} 
              style={{ 
                backgroundColor: '#ef4444', color: '#fff', border: 'none', 
                padding: '8px 20px', borderRadius: '6px', fontWeight: '600', 
                cursor: 'pointer', fontSize: '15px' 
              }}
            >
              Đăng ký
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default Navbar;