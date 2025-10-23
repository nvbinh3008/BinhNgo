package graph; // Khai báo package chứa lớp City

public class City { // Lớp City mô tả một thành phố trong bài toán
    private final int id; // Mã số duy nhất cho mỗi thành phố
    private final String name; // Tên hiển thị của thành phố

    public City(int id, String name) { // Hàm tạo để gán mã số và tên cho thành phố
        this.id = id; // Lưu mã số vào thuộc tính id
        this.name = name; // Lưu tên vào thuộc tính name
    }

    public int getId() { // Trả về mã số của thành phố
        return id; // Hoàn trả giá trị id đã lưu
    }

    public String getName() { // Trả về tên của thành phố
        return name; // Hoàn trả giá trị name đã lưu
    }

    @Override
    public String toString() { // Hiển thị thông tin thành phố dạng chuỗi
        return name; // Chỉ trả về tên vì dễ đọc và ngắn gọn
    }
}
