import axiosClient from "./axiosClient";

const categoryApi = {
    getAll() {
        return axiosClient.get('/categories');
    },
    getExamsByCategory(id, page = 1) {
        return axiosClient.get(`/categories/${id}/exams?page=${page}`);
    }
};

export default categoryApi;