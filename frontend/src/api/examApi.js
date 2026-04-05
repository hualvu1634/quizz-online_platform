import axiosClient from './axiosClient';

const examApi = {
    getExam: (id) => {
        return axiosClient.get(`/exams/${id}`);
    },

    createExam: (data) => {
        return axiosClient.post('/exams', data);
    },
     getAll() {
        return axiosClient.get('/exams');
    },
    updateExam: (id, data) => {
        return axiosClient.put(`/exams/${id}`, data);
    },

    deleteExam: (id) => {
        return axiosClient.delete(`/exams/${id}`);
    },

    checkAnswer: (data) => {
        return axiosClient.post('/exams/check-answer', data);
    },

    submitExam: (data) => {
        return axiosClient.post('/exams/submit', data);
    }
};

export default examApi;