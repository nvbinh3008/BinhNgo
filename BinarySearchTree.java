// Khai báo lớp BinarySearchTree đại diện cho cấu trúc dữ liệu cây tìm kiếm nhị phân
public class BinarySearchTree {
    // Khai báo lớp lồng Node mô tả từng nút trong cây
    private static class Node {
        // Trường dữ liệu lưu trữ giá trị của nút
        int value; // Lưu giá trị khóa của nút
        // Con trỏ tới nút con bên trái
        Node left; // Tham chiếu tới nút con trái
        // Con trỏ tới nút con bên phải
        Node right; // Tham chiếu tới nút con phải

        // Hàm khởi tạo Node với giá trị cho trước
        Node(int value) { // Hàm tạo nhận giá trị
            this.value = value; // Gán giá trị cho trường value
            this.left = null; // Khởi tạo con trái là null
            this.right = null; // Khởi tạo con phải là null
        }
    }

    // Gốc của cây tìm kiếm nhị phân
    private Node root; // Tham chiếu tới nút gốc của cây

    // Hàm khởi tạo cây rỗng
    public BinarySearchTree() { // Hàm tạo mặc định
        this.root = null; // Ban đầu gốc chưa tồn tại
    }

    // Phương thức chèn một giá trị mới vào cây
    public void insert(int value) { // Hàm công khai thêm giá trị
        root = insertRecursive(root, value); // Gọi hàm đệ quy để chèn và cập nhật gốc
    }

    // Hàm trợ giúp đệ quy thực hiện việc chèn
    private Node insertRecursive(Node current, int value) { // Hàm đệ quy thêm nút
        if (current == null) { // Nếu vị trí hiện tại rỗng
            return new Node(value); // Tạo nút mới và trả về để nối vào cây
        } // Kết thúc điều kiện vị trí rỗng

        if (value < current.value) { // Nếu giá trị nhỏ hơn nút hiện tại
            current.left = insertRecursive(current.left, value); // Chèn vào cây con trái
        } else if (value > current.value) { // Nếu giá trị lớn hơn nút hiện tại
            current.right = insertRecursive(current.right, value); // Chèn vào cây con phải
        } // Không xử lý trường hợp bằng nhau để tránh trùng lặp

        return current; // Trả về nút hiện tại sau khi đã chèn xong
    }

    // Phương thức tìm kiếm giá trị trong cây
    public boolean contains(int value) { // Hàm kiểm tra sự tồn tại của giá trị
        return containsRecursive(root, value); // Gọi hàm đệ quy tìm kiếm bắt đầu từ gốc
    }

    // Hàm trợ giúp đệ quy tìm kiếm giá trị
    private boolean containsRecursive(Node current, int value) { // Hàm đệ quy kiểm tra tồn tại
        if (current == null) { // Nếu nút hiện tại rỗng
            return false; // Không tìm thấy giá trị trong cây
        } // Kết thúc điều kiện nút rỗng

        if (value == current.value) { // Nếu giá trị trùng với nút hiện tại
            return true; // Đã tìm thấy giá trị
        } // Kết thúc điều kiện tìm thấy

        if (value < current.value) { // Nếu giá trị nhỏ hơn nút hiện tại
            return containsRecursive(current.left, value); // Tìm trong cây con trái
        } else { // Trường hợp giá trị lớn hơn nút hiện tại
            return containsRecursive(current.right, value); // Tìm trong cây con phải
        }
    }

    // Phương thức xóa một giá trị khỏi cây
    public void delete(int value) { // Hàm công khai xóa giá trị
        root = deleteRecursive(root, value); // Gọi hàm đệ quy xóa và cập nhật gốc
    }

    // Hàm trợ giúp đệ quy thực hiện việc xóa
    private Node deleteRecursive(Node current, int value) { // Hàm đệ quy xóa nút
        if (current == null) { // Nếu cây con rỗng
            return null; // Không có gì để xóa
        } // Kết thúc điều kiện cây con rỗng

        if (value < current.value) { // Nếu giá trị nhỏ hơn nút hiện tại
            current.left = deleteRecursive(current.left, value); // Tiếp tục xóa ở cây con trái
        } else if (value > current.value) { // Nếu giá trị lớn hơn nút hiện tại
            current.right = deleteRecursive(current.right, value); // Tiếp tục xóa ở cây con phải
        } else { // Khi tìm thấy nút cần xóa
            if (current.left == null && current.right == null) { // Nếu nút là lá
                return null; // Xóa bằng cách trả về null để cắt liên kết
            } else if (current.left == null) { // Nếu chỉ có cây con phải
                return current.right; // Thay thế bằng cây con phải
            } else if (current.right == null) { // Nếu chỉ có cây con trái
                return current.left; // Thay thế bằng cây con trái
            } else { // Nếu có cả hai con
                int smallestValue = findMinValue(current.right); // Tìm giá trị nhỏ nhất ở cây con phải
                current.value = smallestValue; // Thay thế giá trị nút hiện tại bằng giá trị nhỏ nhất
                current.right = deleteRecursive(current.right, smallestValue); // Xóa nút chứa giá trị nhỏ nhất ở cây con phải
            }
        }

        return current; // Trả về nút hiện tại sau khi xóa
    }

    // Hàm tìm giá trị nhỏ nhất trong cây con
    private int findMinValue(Node current) { // Hàm tìm giá trị nhỏ nhất
        while (current.left != null) { // Duyệt tới tận cùng bên trái
            current = current.left; // Di chuyển sang con trái
        }
        return current.value; // Trả về giá trị nhỏ nhất tìm được
    }

    // Duyệt cây theo thứ tự trước (pre-order)
    public void traversePreOrder() { // Hàm công khai duyệt tiền tự
        traversePreOrderRecursive(root); // Bắt đầu duyệt từ gốc
    }

    // Hàm trợ giúp đệ quy cho duyệt tiền tự
    private void traversePreOrderRecursive(Node node) { // Hàm đệ quy duyệt tiền tự
        if (node == null) { // Nếu nút hiện tại rỗng
            return; // Kết thúc nhánh
        }
        System.out.print(node.value + " "); // In giá trị nút hiện tại
        traversePreOrderRecursive(node.left); // Duyệt cây con trái
        traversePreOrderRecursive(node.right); // Duyệt cây con phải
    }

    // Duyệt cây theo thứ tự giữa (in-order)
    public void traverseInOrder() { // Hàm công khai duyệt trung tự
        traverseInOrderRecursive(root); // Bắt đầu từ gốc
    }

    // Hàm trợ giúp đệ quy cho duyệt trung tự
    private void traverseInOrderRecursive(Node node) { // Hàm đệ quy duyệt trung tự
        if (node == null) { // Nếu nút rỗng
            return; // Dừng lại
        }
        traverseInOrderRecursive(node.left); // Duyệt cây con trái trước
        System.out.print(node.value + " "); // In giá trị nút hiện tại
        traverseInOrderRecursive(node.right); // Duyệt cây con phải sau
    }

    // Duyệt cây theo thứ tự sau (post-order)
    public void traversePostOrder() { // Hàm công khai duyệt hậu tự
        traversePostOrderRecursive(root); // Bắt đầu từ gốc
    }

    // Hàm trợ giúp đệ quy cho duyệt hậu tự
    private void traversePostOrderRecursive(Node node) { // Hàm đệ quy duyệt hậu tự
        if (node == null) { // Nếu nút rỗng
            return; // Dừng duyệt
        }
        traversePostOrderRecursive(node.left); // Duyệt cây con trái
        traversePostOrderRecursive(node.right); // Duyệt cây con phải
        System.out.print(node.value + " "); // In giá trị nút hiện tại
    }

    // Tìm giá trị nhỏ nhất trong toàn bộ cây
    public Integer findMin() { // Hàm công khai tìm giá trị nhỏ nhất
        if (root == null) { // Nếu cây rỗng
            return null; // Không có giá trị để trả về
        }
        Node current = root; // Bắt đầu từ gốc
        while (current.left != null) { // Di chuyển về phía trái nhất
            current = current.left; // Tiến sang con trái
        }
        return current.value; // Trả về giá trị nhỏ nhất
    }

    // Tìm giá trị lớn nhất trong toàn bộ cây
    public Integer findMax() { // Hàm công khai tìm giá trị lớn nhất
        if (root == null) { // Nếu cây rỗng
            return null; // Không có giá trị để trả về
        }
        Node current = root; // Bắt đầu từ gốc
        while (current.right != null) { // Di chuyển về phía phải nhất
            current = current.right; // Tiến sang con phải
        }
        return current.value; // Trả về giá trị lớn nhất
    }

    // Hàm main minh họa cách sử dụng cây tìm kiếm nhị phân
    public static void main(String[] args) { // Hàm main để chạy thử chương trình
        BinarySearchTree tree = new BinarySearchTree(); // Tạo một cây rỗng mới
        tree.insert(50); // Chèn giá trị 50
        tree.insert(30); // Chèn giá trị 30
        tree.insert(70); // Chèn giá trị 70
        tree.insert(20); // Chèn giá trị 20
        tree.insert(40); // Chèn giá trị 40
        tree.insert(60); // Chèn giá trị 60
        tree.insert(80); // Chèn giá trị 80

        System.out.print("Duyệt trung tự: "); // Thông báo trước khi in duyệt trung tự
        tree.traverseInOrder(); // Gọi duyệt trung tự để in các giá trị có thứ tự tăng dần
        System.out.println(); // Xuống dòng sau khi duyệt trung tự

        System.out.print("Duyệt tiền tự: "); // Thông báo trước khi in duyệt tiền tự
        tree.traversePreOrder(); // Gọi duyệt tiền tự
        System.out.println(); // Xuống dòng sau khi duyệt tiền tự

        System.out.print("Duyệt hậu tự: "); // Thông báo trước khi in duyệt hậu tự
        tree.traversePostOrder(); // Gọi duyệt hậu tự
        System.out.println(); // Xuống dòng sau khi duyệt hậu tự

        System.out.println("Tồn tại 60? " + tree.contains(60)); // Kiểm tra xem 60 có trong cây không
        System.out.println("Tồn tại 25? " + tree.contains(25)); // Kiểm tra xem 25 có trong cây không

        System.out.println("Giá trị nhỏ nhất: " + tree.findMin()); // In giá trị nhỏ nhất trong cây
        System.out.println("Giá trị lớn nhất: " + tree.findMax()); // In giá trị lớn nhất trong cây

        tree.delete(70); // Xóa nút có giá trị 70 khỏi cây
        System.out.print("Duyệt trung tự sau khi xóa 70: "); // Thông báo trước khi duyệt lại
        tree.traverseInOrder(); // Duyệt trung tự để kiểm tra kết quả xóa
        System.out.println(); // Xuống dòng kết thúc
    }
}
