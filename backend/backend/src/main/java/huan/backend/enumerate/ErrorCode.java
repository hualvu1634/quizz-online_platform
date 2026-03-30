package huan.backend.enumerate;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "Lỗi hệ thống"),
    USER_EXISTED(400, "tài khoản đã tồn tại"), 
    LESSON_EXISTED(400, "bài học đã tồn tại"),
    CATEGORY_EXISTED(400, "Danh mục đã tồn tại"),
    EXAM_EXISTED(400,"Khóa học đã tồn tại"),
    USER_NOT_FOUND(404, "Không tìm thấy người dùng"),
    LESSON_NOT_FOUND(404, "Sản phẩm không tồn tại"),
    CATEGORY_NOT_FOUND(404, "Danh mục không tồn tại"),
    EXAM_NOT_FOUND(404,"Bài kiểm tra không tồn tại"),
    QUESTION_NOT_FOUND(404,"Câu hỏi không tồn tại"),
    AUTH_FAILED(401, "Email hoặc mật khẩu không chính xác"), 
    ACCOUNT_LOCKED(403, "Tài khoản đã bị khóa"),
    
    NOT_NULL(400,"Không được để trống"),
    INVALID_SIZE(400, "Độ dài ký tự không hợp lệ"),
    INVALID_MIN(400, "Giá trị đầu vào không hợp lệ"),
    INVALID_PHONE(400, "Định dạng số điện thoại không đúng");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}