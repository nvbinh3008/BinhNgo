// BoatBookingSystem.java tệp chứa tất cả các lớp cho Thuyền Đặt chỗ Hệ thống ứng dụng.
import java.io.BufferedReader; // Nhập BufferedReader cho đọc văn bản từ một tệp.
import java.io.BufferedWriter; // Nhập BufferedWriter cho ghi văn bản để một tệp.
import java.io.FileReader; // Nhập FileReader để đọc từ tệp.
import java.io.FileWriter; // Nhập FileWriter để ghi để tệp.
import java.io.IOException; // Nhập IOException để xử lý nhập/đầu ra ngoại lệ.
import java.util.ArrayDeque; // Nhập ArrayDeque để triển khai hàng đợi được sử dụng trong BFS duyệt.
import java.util.Deque; // Nhập Deque giao diện cho hàng đợi tác vụ.
import java.util.InputMismatchException; // Nhập InputMismatchException để xử lý không hợp lệ nhập kiểu.
import java.util.Scanner; // Nhập Scanner cho tương tác bảng điều khiển nhập.

// Thuyền lớp lưu trữ thông tin về một thuyền thực thể.
class Boat {
    // Trường lưu thuyền mã mà duy nhất nhận dạng thuyền.
    String bcode; // Thuyền mã trường.
    // Trường lưu thuyền tên.
    String boatName; // Thuyền tên trường.
    // Trường lưu tổng số của ghế trên thuyền.
    int seat; // Tổng ghế đếm trường.
    // Trường lưu số của đã đặt ghế.
    int booked; // Đã đặt ghế đếm trường.
    // Trường lưu khởi hành địa điểm của thuyền.
    String departPlace; // Khởi hành địa điểm trường.
    // Trường lưu đánh giá của thuyền.
    double rate; // Đánh giá trường.

    // Hàm khởi tạo được sử dụng để tạo một thuyền với tất cả cần thiết thuộc tính.
    Boat(String bcode, String boatName, int seat, int booked, String departPlace, double rate) {
        this.bcode = bcode; // Gán thuyền mã để đối tượng.
        this.boatName = boatName; // Gán thuyền tên để đối tượng.
        this.seat = seat; // Gán tổng ghế để đối tượng.
        this.booked = booked; // Gán đã đặt ghế để đối tượng.
        this.departPlace = departPlace; // Gán khởi hành địa điểm để đối tượng.
        this.rate = rate; // Gán đánh giá để đối tượng.
    }

    // Phương thức chuyển đổi thuyền để một định dạng chuỗi biểu diễn.
    /* Bước 1: Bắt đầu xây dựng một StringBuilder để lưu dạng văn bản biểu diễn.
       Bước 2: Thêm mỗi trường tách bằng phân tách.
       Bước 3: Chuyển đổi bộ dựng để một chuỗi và trả về nó. */
    public String toDataString() {
        StringBuilder builder = new StringBuilder(); // Tạo một mới StringBuilder thực thể.
        builder.append(bcode); // Nối thuyền mã để bộ dựng.
        builder.append("|"); // Nối một phân tách.
        builder.append(boatName); // Nối thuyền tên để bộ dựng.
        builder.append("|"); // Nối một phân tách.
        builder.append(seat); // Nối ghế số để bộ dựng.
        builder.append("|"); // Nối một phân tách.
        builder.append(booked); // Nối đã đặt ghế để bộ dựng.
        builder.append("|"); // Nối một phân tách.
        builder.append(departPlace); // Nối khởi hành địa điểm.
        builder.append("|"); // Nối một phân tách.
        builder.append(rate); // Nối đánh giá giá trị.
        return builder.toString(); // Trả về được xây dựng chuỗi.
    }

    // Phương thức tạo một con người-dễ đọc mô tả của thuyền.
    /* Bước 1: Bắt đầu xây dựng một StringBuilder cho mô tả.
       Bước 2: Thêm mỗi trường với labels cho clarity.
       Bước 3: Trả về định dạng chuỗi. */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(); // Tạo một mới StringBuilder thực thể.
        builder.append(String.format("%-10s", bcode)); // Nối thuyền mã định dạng với đệm.
        builder.append(String.format("%-20s", boatName)); // Nối thuyền tên với đệm.
        builder.append(String.format("%-8d", seat)); // Nối ghế đếm định dạng.
        builder.append(String.format("%-8d", booked)); // Nối đã đặt đếm định dạng.
        builder.append(String.format("%-20s", departPlace)); // Nối khởi hành địa điểm định dạng.
        builder.append(String.format("%-10.2f", rate)); // Nối đánh giá định dạng với hai số thập phân.
        return builder.toString(); // Trả về bao gồm chuỗi.
    }
}

// BoatNode lớp đại diện một nút trong nhị phân tìm kiếm cây lưu Thuyền đối tượng.
class BoatNode {
    // Trường lưu thuyền thông tin.
    Boat info; // Thuyền đối tượng được lưu trong nút.
    // Trường lưu tham chiếu để trái con.
    BoatNode left; // Trái con tham chiếu.
    // Trường lưu tham chiếu để phải con.
    BoatNode right; // Phải con tham chiếu.

    // Hàm khởi tạo tạo một nút với đã cho thuyền dữ liệu.
    BoatNode(Boat info) {
        this.info = info; // Gán thuyền thông tin để nút.
        this.left = null; // Khởi tạo trái con tham chiếu để rỗng.
        this.right = null; // Khởi tạo phải con tham chiếu để rỗng.
    }
}

// BoatBST lớp triển khai nhị phân tìm kiếm cây lưu thuyền khóa bằng bcode.
class BoatBST {
    // Trường lưu gốc của nhị phân tìm kiếm cây.
    BoatNode root; // Gốc nút của cây.

    // Hàm khởi tạo khởi tạo một trống cây.
    BoatBST() {
        this.root = null; // Thiết lập gốc để rỗng để đại diện một trống cây.
    }

    // Phương thức để chèn một mới thuyền vào cây.
    /* Bước 1: Tạo một mới Thuyền đối tượng từ nhập parameters.
       Bước 2: Nếu cây là trống, assign mới nút như gốc.
       Bước 3: Otherwise, traverse cây sử dụng bcode comparisons.
       Bước 4: Nếu một trùng bcode là tìm thấy, do không chèn và trả về sai.
       Bước 5: Chèn mới nút tại chính xác lá vị trí và trả về đúng. */
    public boolean insert(String bcode, String boatName, int seat, int booked, String departPlace, double rate) {
        Boat newBoat = new Boat(bcode, boatName, seat, booked, departPlace, rate); // Tạo một mới thuyền đối tượng với được cung cấp thông tin.
        BoatNode newNode = new BoatNode(newBoat); // Bao bọc thuyền bên trong một cây nút.
        if (root == null) { // Kiểm tra nếu cây là hiện đang trống.
            root = newNode; // Gán mới nút như gốc của cây.
            return true; // Trả về đúng để cho biết thành công chèn.
        }
        BoatNode parent = null; // Biến lưu cha trong khi duyệt.
        BoatNode current = root; // Bắt đầu duyệt từ gốc nút.
        while (current != null) { // Tiếp tục cho đến khi một rỗng con là tìm thấy.
            parent = current; // Cập nhật cha để hiện tại nút.
            int cmp = bcode.compareTo(current.info.bcode); // So sánh mới bcode với hiện tại của nút bcode.
            if (cmp == 0) { // Kiểm tra cho trùng bcode.
                return false; // Trả về sai bởi vì trùng lặp là không được phép.
            } else if (cmp < 0) { // Kiểm tra nếu mới bcode là nhỏ hơn hơn hiện tại bcode.
                current = current.left; // Di chuyển duyệt để trái con.
            } else { // Xử lý trường hợp nơi mới bcode là lớn hơn hơn hiện tại bcode.
                current = current.right; // Di chuyển duyệt để phải con.
            }
        }
        if (bcode.compareTo(parent.info.bcode) < 0) { // Kiểm tra nếu mới bcode là nhỏ hơn hơn của cha bcode.
            parent.left = newNode; // Thiết lập mới nút như trái con.
        } else { // Xử lý trường hợp nơi mới bcode là lớn hơn hơn cha bcode.
            parent.right = newNode; // Thiết lập mới nút như phải con.
        }
        return true; // Trả về đúng để cho biết thành công chèn.
    }

    // Phương thức để thực hiện một trong-thứ tự duyệt và in thuyền thông tin để bảng điều khiển.
    /* Bước 1: Bắt đầu duyệt từ gốc nút.
       Bước 2: Đệ quy visit trái cây con, hiện tại nút, then phải cây con.
       Bước 3: In mỗi visited của nút dữ liệu để bảng điều khiển. */
    public void inOrderTraversal() {
        inOrderTraversal(root); // Gọi phụ trợ phương thức với gốc nút.
    }

    // Phụ trợ phương thức thực thi đệ quy trong-thứ tự duyệt.
    /* Bước 1: Nếu hiện tại nút là rỗng, trả về để stop đệ quy.
       Bước 2: Đệ quy traverse trái cây con.
       Bước 3: Quy trình hiện tại nút bằng in nó.
       Bước 4: Đệ quy traverse phải cây con. */
    private void inOrderTraversal(BoatNode node) {
        if (node == null) { // Kiểm tra nếu hiện tại nút là rỗng.
            return; // Thoát đệ quy khi đạt một lá.
        }
        inOrderTraversal(node.left); // Đệ quy traversing trái cây con.
        System.out.println(node.info); // In hiện tại của nút thông tin.
        inOrderTraversal(node.right); // Đệ quy traversing phải cây con.
    }

    // Phương thức performing breadth-đầu tiên duyệt và in dữ liệu.
    /* Bước 1: Nếu cây là trống, stop duyệt.
       Bước 2: Initialize một hàng đợi và thêm gốc nút.
       Bước 3: While hàng đợi là không trống, xóa một nút.
       Bước 4: In nút và enqueue của nó con nếu they tồn tại. */
    public void breadthFirstTraversal() {
        if (root == null) { // Kiểm tra nếu cây là trống.
            return; // Kết thúc duyệt như ở đó là no nút để quy trình.
        }
        Deque<BoatNode> queue = new ArrayDeque<>(); // Tạo một hàng đợi để lưu nút cho BFS.
        queue.add(root); // Thêm gốc nút để hàng đợi.
        while (!queue.isEmpty()) { // Tiếp tục cho đến khi tất cả nút là processed.
            BoatNode node = queue.poll(); // Lấy và loại bỏ đầu nút từ hàng đợi.
            System.out.println(node.info); // In thuyền thông tin được lưu trong nút.
            if (node.left != null) { // Kiểm tra nếu trái con tồn tại.
                queue.add(node.left); // Thêm trái con để hàng đợi.
            }
            if (node.right != null) { // Kiểm tra nếu phải con tồn tại.
                queue.add(node.right); // Thêm phải con để hàng đợi.
            }
        }
    }

    // Phương thức để ghi một trong-thứ tự duyệt để một tệp.
    /* Bước 1: Mở một BufferedWriter cho mục tiêu tệp.
       Bước 2: Đệ quy thực hiện trong-thứ tự duyệt và thêm mỗi nút để tệp.
       Bước 3: Đóng writer để persist dữ liệu. */
    public void inOrderToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) { // Đang mở writer trong một thử-với-tài nguyên khối.
            inOrderToFile(root, writer); // Gọi phụ trợ phương thức để ghi nút.
        }
    }

    // Phụ trợ phương thức performing đệ quy trong-thứ tự duyệt và ghi để một tệp.
    /* Bước 1: Nếu hiện tại nút là rỗng, trả về.
       Bước 2: Đệ quy quy trình trái cây con.
       Bước 3: Ghi hiện tại nút dữ liệu để tệp followed bằng một dòng mới.
       Bước 4: Đệ quy quy trình phải cây con. */
    private void inOrderToFile(BoatNode node, BufferedWriter writer) throws IOException {
        if (node == null) { // Kiểm tra nếu nút là rỗng.
            return; // Kết thúc đệ quy cho rỗng nút.
        }
        inOrderToFile(node.left, writer); // Ghi trái cây con để tệp.
        writer.write(node.info.toDataString()); // Ghi hiện tại của nút dữ liệu chuỗi để tệp.
        writer.newLine(); // Chèn một mới dòng sau hiện tại bản ghi.
        inOrderToFile(node.right, writer); // Ghi phải cây con để tệp.
    }

    // Phương thức để tìm kiếm cho một thuyền bằng bcode.
    /* Bước 1: Bắt đầu duyệt tại gốc nút.
       Bước 2: So sánh tìm kiếm mã với hiện tại nút.
       Bước 3: Di chuyển trái hoặc phải dựa trên so sánh.
       Bước 4: Trả về nút khi tìm thấy hoặc rỗng nếu không có. */
    public BoatNode search(String bcode) {
        BoatNode current = root; // Bắt đầu từ gốc nút.
        while (current != null) { // Tiếp tục cho đến khi nút là tìm thấy hoặc duyệt kết thúc.
            int cmp = bcode.compareTo(current.info.bcode); // So sánh tìm kiếm mã với hiện tại của nút mã.
            if (cmp == 0) { // Kiểm tra nếu mã là bằng nhau.
                return current; // Trả về hiện tại nút bởi vì thuyền là tìm thấy.
            } else if (cmp < 0) { // Kiểm tra nếu tìm kiếm mã là nhỏ hơn.
                current = current.left; // Di chuyển để trái con để tiếp tục tìm kiếm.
            } else { // Xử lý trường hợp nơi tìm kiếm mã là lớn hơn.
                current = current.right; // Di chuyển để phải con để tiếp tục tìm kiếm.
            }
        }
        return null; // Trả về rỗng để cho biết thuyền đã không tìm thấy.
    }

    // Phương thức xóa một thuyền bằng bcode sử dụng sao chép-và-xóa technique.
    /* Bước 1: Định vị nút và của nó cha.
       Bước 2: Nếu nút có hai con, tìm inorder tiền nhiệm, sao chép dữ liệu, và xóa tiền nhiệm.
       Bước 3: Nếu nút có one con, replace nó với của nó con.
        Bước 4: Nếu nút là một lá, xóa nó trực tiếp.
       Bước 5: Cập nhật cha liên kết accordingly. */
    public boolean deleteByCopying(String bcode) {
        BoatNode parent = null; // Biến để giữ theo dõi của cha nút.
        BoatNode current = root; // Bắt đầu tìm kiếm từ gốc nút.
        while (current != null && !current.info.bcode.equals(bcode)) { // Lặp cho đến khi mục tiêu nút là tìm thấy.
            parent = current; // Thiết lập cha để hiện tại nút trước di chuyển xuống.
            if (bcode.compareTo(current.info.bcode) < 0) { // Kiểm tra nếu mục tiêu mã là nhỏ hơn hơn hiện tại.
                current = current.left; // Di chuyển để trái con.
            } else { // Xử lý trường hợp nơi mục tiêu mã là lớn hơn.
                current = current.right; // Di chuyển để phải con.
            }
        }
        if (current == null) { // Kiểm tra nếu mục tiêu nút đã tìm thấy.
            return false; // Trả về sai bởi vì no nút với mã tồn tại.
        }
        if (current.left != null && current.right != null) { // Kiểm tra nếu nút có hai con.
            BoatNode predecessorParent = current; // Lưu cha của tiền nhiệm.
            BoatNode predecessor = current.left; // Bắt đầu tìm kiếm cho tiền nhiệm trong trái cây con.
            while (predecessor.right != null) { // Looking cho phải nhất nút trong trái cây con.
                predecessorParent = predecessor; // Cập nhật cha để hiện tại tiền nhiệm nút.
                predecessor = predecessor.right; // Di chuyển để phải con.
            }
            current.info = predecessor.info; // Copying predecessor's dữ liệu vào mục tiêu nút.
            if (predecessorParent == current) { // Kiểm tra nếu tiền nhiệm là trực tiếp trái con.
                predecessorParent.left = predecessor.left; // Replacing trái con với predecessor's trái cây con.
            } else { // Xử lý trường hợp nơi tiền nhiệm là sâu hơn trong cây con.
                predecessorParent.right = predecessor.left; // Replacing tiền nhiệm nút với của nó trái con.
            }
            return true; // Trả về đúng để cho biết thành công xóa.
        }
        BoatNode child = (current.left != null) ? current.left : current.right; // Xác định con nút nếu bất kỳ.
        if (parent == null) { // Kiểm tra nếu nút để xóa là gốc.
            root = child; // Cập nhật gốc để con nút.
        } else if (parent.left == current) { // Kiểm tra nếu hiện tại là trái con của của nó cha.
            parent.left = child; // Connecting của cha trái pointer để con.
        } else { // Xử lý trường hợp nơi hiện tại là phải con của của nó cha.
            parent.right = child; // Connecting của cha phải pointer để con.
        }
        return true; // Trả về đúng để cho biết thành công xóa.
    }

    // Phương thức để chuyển đổi cây vào một cân bằng cây sử dụng một mảng trung gian.
    /* Bước 1: Traverse cây trong-thứ tự để lưu nút trong một danh sách.
       Bước 2: Đệ quy xây dựng một cân bằng cây từ được sắp xếp danh sách.
       Bước 3: Assign mới gốc để cây. */
    public void balance() {
        java.util.List<Boat> boats = new java.util.ArrayList<>(); // Tạo một danh sách để lưu thuyền trong được sắp xếp thứ tự.
        storeInOrder(root, boats); // Điền danh sách với thuyền sử dụng trong-thứ tự duyệt.
        root = buildBalancedTree(boats, 0, boats.size() - 1); // Xây dựng một cân bằng cây và gán mới gốc.
    }

    // Phụ trợ phương thức để lưu thuyền trong-thứ tự vào một danh sách.
    /* Bước 1: Nếu nút là rỗng, trả về immediately.
       Bước 2: Đệ quy quy trình trái cây con.
       Bước 3: Thêm hiện tại của nút thuyền để danh sách.
       Bước 4: Đệ quy quy trình phải cây con. */
    private void storeInOrder(BoatNode node, java.util.List<Boat> boats) {
        if (node == null) { // Kiểm tra nếu nút là rỗng.
            return; // Kết thúc đệ quy cho rỗng nút.
        }
        storeInOrder(node.left, boats); // Xử lý trái cây con.
        boats.add(node.info); // Thêm hiện tại thuyền để danh sách.
        storeInOrder(node.right, boats); // Xử lý phải cây con.
    }

    // Phụ trợ phương thức để xây dựng một cân bằng cây từ một danh sách của thuyền.
    /* Bước 1: Nếu bắt đầu chỉ số exceeds kết thúc chỉ số, trả về rỗng để kết thúc đệ quy.
       Bước 2: Calculate middle chỉ số để select gốc cho hiện tại cây con.
       Bước 3: Tạo một nút sử dụng middle phần tử.
       Bước 4: Đệ quy xây dựng trái và phải subtrees từ subranges.
       Bước 5: Trả về được xây dựng nút. */
    private BoatNode buildBalancedTree(java.util.List<Boat> boats, int start, int end) {
        if (start > end) { // Kiểm tra nếu mảng con là trống.
            return null; // Trả về rỗng cho trống phạm vi.
        }
        int mid = (start + end) / 2; // Calculating middle chỉ số.
        BoatNode node = new BoatNode(boats.get(mid)); // Tạo một nút với middle thuyền.
        node.left = buildBalancedTree(boats, start, mid - 1); // Đệ quy xây dựng trái cây con.
        node.right = buildBalancedTree(boats, mid + 1, end); // Đệ quy xây dựng phải cây con.
        return node; // Trả về được xây dựng nút.
    }

    // Phương thức counting số của thuyền được lưu trong cây.
    /* Bước 1: Bắt đầu từ gốc và đệ quy đếm nút.
       Bước 2: Cho mỗi nút, đếm one cộng số đếm của của nó subtrees.
       Bước 3: Trả về tổng đếm. */
    public int countBoats() {
        return countBoats(root); // Gọi phụ trợ phương thức bắt đầu từ gốc.
    }

    // Phụ trợ phương thức để đệ quy đếm nút trong cây.
    /* Bước 1: Nếu nút là rỗng, trả về không.
       Bước 2: Đệ quy đếm trái cây con.
       Bước 3: Đệ quy đếm phải cây con.
       Bước 4: Sum số đếm và thêm one cho hiện tại nút. */
    private int countBoats(BoatNode node) {
        if (node == null) { // Kiểm tra nếu nút là rỗng.
            return 0; // Trả về không cho rỗng nút.
        }
        return 1 + countBoats(node.left) + countBoats(node.right); // Cộng số đếm từ trái và phải cộng hiện tại nút.
    }

    // Phương thức tải thuyền dữ liệu từ một tệp và chèn vào cây.
    /* Bước 1: Mở tệp sử dụng BufferedReader.
       Bước 2: Đọc mỗi dòng, parse thuyền thuộc tính, và chèn vào cây.
       Bước 3: Ignore không hợp lệ dòng và tiếp tục xử lý còn lại dòng.
       Bước 4: Đóng reader sau finishing. */
    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { // Đang mở tệp cho đọc sử dụng thử-với-tài nguyên.
            String line; // Biến để hold mỗi dòng từ tệp.
            while ((line = reader.readLine()) != null) { // Đọc tệp dòng bằng dòng.
                String[] parts = line.split("\\|"); // Splitting dòng vào phần sử dụng phân tách.
                if (parts.length != 6) { // Kiểm tra nếu dòng có chính xác số của phần.
                    continue; // Bỏ qua malformed dòng.
                }
                String bcode = parts[0].trim(); // Tách và cắt bỏ thuyền mã.
                String name = parts[1].trim(); // Tách và cắt bỏ thuyền tên.
                int seat = Integer.parseInt(parts[2].trim()); // Phân tích ghế đếm như integer.
                int booked = Integer.parseInt(parts[3].trim()); // Phân tích đã đặt đếm như integer.
                String depart = parts[4].trim(); // Tách và cắt bỏ khởi hành địa điểm.
                double rate = Double.parseDouble(parts[5].trim()); // Phân tích đánh giá như số thực.
                insert(bcode, name, seat, booked, depart, rate); // Chèn phân tích thuyền vào cây.
            }
        }
    }
}

// Khách hàng lớp lưu trữ thông tin về khách hàng sử dụng hệ thống.
class Customer {
    // Trường lưu duy nhất khách hàng mã.
    String ccode; // Khách hàng mã trường.
    // Trường lưu tên của khách hàng.
    String cusName; // Khách hàng tên trường.
    // Trường lưu điện thoại số.
    String phone; // Điện thoại số trường.

    // Hàm khởi tạo để initialize một mới khách hàng đối tượng.
    Customer(String ccode, String cusName, String phone) {
        this.ccode = ccode; // Gán khách hàng mã để đối tượng.
        this.cusName = cusName; // Gán khách hàng tên để đối tượng.
        this.phone = phone; // Gán điện thoại số để đối tượng.
    }

    // Phương thức chuyển đổi khách hàng để một dữ liệu chuỗi cho tệp lưu trữ.
    /* Bước 1: Bắt đầu một StringBuilder thực thể.
       Bước 2: Thêm mỗi trường tách bằng một phân tách.
       Bước 3: Trả về được xây chuỗi. */
    public String toDataString() {
        StringBuilder builder = new StringBuilder(); // Tạo một mới StringBuilder thực thể.
        builder.append(ccode); // Nối khách hàng mã để bộ dựng.
        builder.append("|"); // Nối một phân tách.
        builder.append(cusName); // Nối khách hàng tên để bộ dựng.
        builder.append("|"); // Nối một phân tách.
        builder.append(phone); // Nối điện thoại số để bộ dựng.
        return builder.toString(); // Trả về được xây chuỗi.
    }

    // Phương thức tạo một con người-dễ đọc mô tả của khách hàng.
    /* Bước 1: Tạo một StringBuilder cho định dạng đầu ra.
       Bước 2: Thêm mỗi trường với alignment formatting.
       Bước 3: Trả về final chuỗi. */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(); // Tạo một mới StringBuilder thực thể.
        builder.append(String.format("%-10s", ccode)); // Nối khách hàng mã với đệm.
        builder.append(String.format("%-20s", cusName)); // Nối khách hàng tên với đệm.
        builder.append(String.format("%-15s", phone)); // Nối điện thoại số với đệm.
        return builder.toString(); // Trả về bao gồm chuỗi.
    }
}

// CustomerNode lớp đại diện một nút trong đơn liên kết danh sách cho khách hàng.
class CustomerNode {
    // Trường lưu khách hàng thông tin.
    Customer info; // Khách hàng đối tượng được lưu trong this nút.
    // Trường lưu tham chiếu để tiếp theo nút trong danh sách.
    CustomerNode next; // Tham chiếu để tiếp theo nút.

    // Hàm khởi tạo tạo một nút với khách hàng dữ liệu.
    CustomerNode(Customer info) {
        this.info = info; // Gán khách hàng thông tin để nút.
        this.next = null; // Khởi tạo tiếp theo tham chiếu để rỗng.
    }
}

// CustomerList lớp manages một đơn liên kết danh sách của khách hàng.
class CustomerList {
    // Trường lưu đầu của danh sách.
    CustomerNode head; // Đầu nút của danh sách.
    // Trường lưu đuôi của danh sách cho efficient thêm tác vụ.
    CustomerNode tail; // Đuôi nút của danh sách.

    // Hàm khởi tạo khởi tạo một trống danh sách.
    CustomerList() {
        this.head = null; // Thiết lập đầu để rỗng cho một trống danh sách.
        this.tail = null; // Thiết lập đuôi để rỗng cho một trống danh sách.
    }

    // Phương thức để thêm một mới khách hàng để kết thúc của danh sách.
    /* Bước 1: Tạo một mới nút cho khách hàng.
       Bước 2: Nếu danh sách là trống, set đầu và đuôi để mới nút.
       Bước 3: Otherwise, attach mới nút để đuôi và cập nhật đuôi.
       Bước 4: Trả về đúng khi chèn succeeds. */
    public boolean addLast(String ccode, String cusName, String phone) {
        Customer newCustomer = new Customer(ccode, cusName, phone); // Tạo một mới khách hàng đối tượng với được cung cấp dữ liệu.
        if (findByCode(ccode) != null) { // Kiểm tra nếu một khách hàng với giống mã đã tồn tại.
            return false; // Trả về sai để tránh trùng bản ghi.
        }
        CustomerNode node = new CustomerNode(newCustomer); // Tạo một mới nút để lưu khách hàng.
        if (head == null) { // Kiểm tra nếu danh sách là hiện đang trống.
            head = node; // Thiết lập đầu để mới nút.
            tail = node; // Thiết lập đuôi để mới nút.
        } else { // Xử lý trường hợp nơi danh sách đã có elements.
            tail.next = node; // Attaching mới nút sau hiện tại đuôi.
            tail = node; // Cập nhật đuôi tham chiếu để mới nút.
        }
        return true; // Trả về đúng để cho biết thành công bổ sung.
    }

    // Phương thức để hiển thị tất cả khách hàng trong danh sách.
    /* Bước 1: Bắt đầu từ đầu nút.
       Bước 2: Traverse danh sách nút bằng nút.
       Bước 3: In mỗi customer's thông tin. */
    public void display() {
        CustomerNode current = head; // Bắt đầu duyệt từ đầu.
        while (current != null) { // Tiếp tục cho đến khi đạt kết thúc của danh sách.
            System.out.println(current.info); // In hiện tại customer's thông tin.
            current = current.next; // Di chuyển để tiếp theo nút trong danh sách.
        }
    }

    // Phương thức để tìm một khách hàng bằng mã.
    /* Bước 1: Bắt đầu duyệt tại đầu.
       Bước 2: So sánh mỗi của nút mã với mục tiêu.
       Bước 3: Trả về nút khi một khớp là tìm thấy hoặc rỗng nếu không có. */
    public CustomerNode findByCode(String ccode) {
        CustomerNode current = head; // Bắt đầu duyệt từ đầu nút.
        while (current != null) { // Tiếp tục cho đến khi kết thúc của danh sách.
            if (current.info.ccode.equals(ccode)) { // Kiểm tra nếu hiện tại của nút mã matches mục tiêu.
                return current; // Trả về nút khi một khớp là tìm thấy.
            }
            current = current.next; // Di chuyển để tiếp theo nút khi không được khớp.
        }
        return null; // Trả về rỗng nếu no đang khớp khách hàng là tìm thấy.
    }

    // Phương thức để xóa một khách hàng bằng mã.
    /* Bước 1: Xử lý trống danh sách bằng trả về sai.
       Bước 2: Nếu đầu matches mã, xóa nó và adjust pointers.
       Bước 3: Otherwise, traverse để tìm previous nút của mục tiêu.
       Bước 4: Cập nhật liên kết để exclude mục tiêu nút và adjust đuôi nếu cần thiết. */
    public boolean deleteByCode(String ccode) {
        if (head == null) { // Kiểm tra nếu danh sách là trống.
            return false; // Trả về sai bởi vì ở đó là không có gì để xóa.
        }
        if (head.info.ccode.equals(ccode)) { // Kiểm tra nếu đầu nút matches mã.
            head = head.next; // Di chuyển đầu để tiếp theo nút để xóa đầu tiên nút.
            if (head == null) { // Kiểm tra nếu danh sách became trống sau xóa.
                tail = null; // Thiết lập đuôi để rỗng khi danh sách là trống.
            }
            return true; // Trả về đúng để cho biết thành công xóa.
        }
        CustomerNode current = head; // Bắt đầu duyệt từ đầu nút.
        while (current.next != null && !current.next.info.ccode.equals(ccode)) { // Looking phía trước để tìm nút trước mục tiêu.
            current = current.next; // Di chuyển để tiếp theo nút.
        }
        if (current.next == null) { // Kiểm tra nếu mục tiêu nút đã không tìm thấy.
            return false; // Trả về sai bởi vì xóa không thể xảy ra.
        }
        if (current.next == tail) { // Kiểm tra nếu nút để xóa là đuôi.
            tail = current; // Cập nhật đuôi tham chiếu để hiện tại nút.
        }
        current.next = current.next.next; // Bypassing mục tiêu nút để xóa nó từ danh sách.
        return true; // Trả về đúng để cho biết thành công xóa.
    }

    // Phương thức để tải khách hàng từ một tệp và thêm để danh sách.
    /* Bước 1: Mở tệp sử dụng BufferedReader.
       Bước 2: Đọc mỗi dòng, parse khách hàng thuộc tính, và thêm để danh sách.
       Bước 3: Skip không hợp lệ dòng.
       Bước 4: Đóng reader automatically. */
    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { // Đang mở tệp trong một thử-với-tài nguyên khối.
            String line; // Biến để hold mỗi dòng.
            while ((line = reader.readLine()) != null) { // Đọc dòng cho đến khi đạt kết thúc của tệp.
                String[] parts = line.split("\\|"); // Splitting dòng sử dụng phân tách.
                if (parts.length != 3) { // Kiểm tra nếu dòng có chính xác ba phần.
                    continue; // Bỏ qua malformed dòng.
                }
                String ccode = parts[0].trim(); // Tách khách hàng mã.
                String name = parts[1].trim(); // Tách khách hàng tên.
                String phone = parts[2].trim(); // Tách điện thoại số.
                addLast(ccode, name, phone); // Thêm khách hàng để danh sách.
            }
        }
    }

    // Phương thức để save khách hàng danh sách để một tệp.
    /* Bước 1: Mở mục tiêu tệp sử dụng BufferedWriter.
       Bước 2: Traverse danh sách và ghi mỗi khách hàng như một dữ liệu chuỗi.
       Bước 3: Đóng writer automatically. */
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) { // Đang mở writer trong một thử-với-tài nguyên khối.
            CustomerNode current = head; // Bắt đầu duyệt từ đầu.
            while (current != null) { // Tiếp tục cho đến khi kết thúc của danh sách.
                writer.write(current.info.toDataString()); // Ghi hiện tại customer's dữ liệu chuỗi.
                writer.newLine(); // Thêm một dòng mới sau mỗi bản ghi.
                current = current.next; // Di chuyển để tiếp theo nút trong danh sách.
            }
        }
    }
}

// Đặt chỗ lớp lưu trữ thông tin về một đặt chỗ giao dịch.
class Booking {
    // Trường lưu thuyền mã cho đặt chỗ.
    String bcode; // Thuyền mã trường.
    // Trường lưu khách hàng mã liên quan với đặt chỗ.
    String ccode; // Khách hàng mã trường.
    // Trường lưu số của ghế đã đặt.
    int seat; // Ghế đã đặt trường.

    // Hàm khởi tạo để initialize đặt chỗ dữ liệu.
    Booking(String bcode, String ccode, int seat) {
        this.bcode = bcode; // Gán thuyền mã để đặt chỗ.
        this.ccode = ccode; // Gán khách hàng mã để đặt chỗ.
        this.seat = seat; // Gán số của ghế để đặt chỗ.
    }

    // Phương thức chuyển đổi đặt chỗ để một dữ liệu chuỗi cho tệp.
    /* Bước 1: Bắt đầu một StringBuilder thực thể.
       Bước 2: Thêm mỗi trường tách bằng một phân tách.
       Bước 3: Trả về resulting chuỗi. */
    public String toDataString() {
        StringBuilder builder = new StringBuilder(); // Tạo một mới StringBuilder thực thể.
        builder.append(bcode); // Nối thuyền mã.
        builder.append("|"); // Nối một phân tách.
        builder.append(ccode); // Nối khách hàng mã.
        builder.append("|"); // Nối một phân tách.
        builder.append(seat); // Nối ghế đếm.
        return builder.toString(); // Trả về chuỗi biểu diễn.
    }

    // Phương thức chuyển đổi đặt chỗ để một dễ đọc chuỗi.
    /* Bước 1: Tạo một StringBuilder cho formatting.
       Bước 2: Thêm mỗi trường với alignment formatting.
       Bước 3: Trả về định dạng chuỗi. */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(); // Tạo một mới StringBuilder thực thể.
        builder.append(String.format("%-10s", bcode)); // Nối thuyền mã với đệm.
        builder.append(String.format("%-10s", ccode)); // Nối khách hàng mã với đệm.
        builder.append(String.format("%-8d", seat)); // Nối ghế đếm với đệm.
        return builder.toString(); // Trả về định dạng chuỗi.
    }
}

// BookingNode lớp đại diện một nút trong đặt chỗ liên kết danh sách.
class BookingNode {
    // Trường lưu đặt chỗ thông tin.
    Booking info; // Đặt chỗ đối tượng được lưu trong nút.
    // Trường lưu tham chiếu để tiếp theo nút.
    BookingNode next; // Tham chiếu để tiếp theo nút.

    // Hàm khởi tạo tạo một nút với đặt chỗ dữ liệu.
    BookingNode(Booking info) {
        this.info = info; // Gán đặt chỗ để nút.
        this.next = null; // Khởi tạo tiếp theo tham chiếu để rỗng.
    }
}

// BookingList lớp manages đặt chỗ bản ghi trong một đơn liên kết danh sách.
class BookingList {
    // Trường lưu đầu của đặt chỗ danh sách.
    BookingNode head; // Đầu nút của danh sách.
    // Trường lưu đuôi của đặt chỗ danh sách cho efficient thêm tác vụ.
    BookingNode tail; // Đuôi nút của danh sách.

    // Hàm khởi tạo khởi tạo một trống đặt chỗ danh sách.
    BookingList() {
        this.head = null; // Thiết lập đầu để rỗng cho một trống danh sách.
        this.tail = null; // Thiết lập đuôi để rỗng cho một trống danh sách.
    }

    // Phương thức để thêm một mới đặt chỗ sau xác thực constraints.
    /* Bước 1: Validate mà thuyền tồn tại trong cây.
       Bước 2: Validate mà khách hàng tồn tại trong danh sách.
       Bước 3: Check available ghế trên thuyền.
       Bước 4: Deduct ghế và thêm đặt chỗ để danh sách.
       Bước 5: Trả về đúng nếu đặt chỗ succeeds. */
    public boolean addBooking(BoatBST boatTree, CustomerList customerList, String bcode, String ccode, int seat) {
        BoatNode boatNode = boatTree.search(bcode); // Tìm kiếm cho thuyền sử dụng được cung cấp mã.
        if (boatNode == null) { // Kiểm tra nếu thuyền tồn tại.
            return false; // Trả về sai khi thuyền là không có.
        }
        CustomerNode customerNode = customerList.findByCode(ccode); // Tìm kiếm cho khách hàng.
        if (customerNode == null) { // Kiểm tra nếu khách hàng tồn tại.
            return false; // Trả về sai khi khách hàng là không có.
        }
        if (seat <= 0) { // Xác thực mà được yêu cầu ghế là dương.
            return false; // Trả về sai cho không hợp lệ ghế đếm.
        }
        int available = boatNode.info.seat - boatNode.info.booked; // Calculating còn lại available ghế.
        if (seat > available) { // Kiểm tra nếu ở đó là enough ghế.
            return false; // Trả về sai nếu không enough ghế giữ lại.
        }
        boatNode.info.booked += seat; // Tăng đã đặt ghế trên thuyền.
        Booking booking = new Booking(bcode, ccode, seat); // Tạo một mới đặt chỗ đối tượng.
        BookingNode node = new BookingNode(booking); // Bao bọc đặt chỗ trong một danh sách nút.
        if (head == null) { // Kiểm tra nếu đặt chỗ danh sách là trống.
            head = node; // Thiết lập đầu để mới nút.
            tail = node; // Thiết lập đuôi để mới nút.
        } else { // Xử lý trường hợp nơi danh sách đã có elements.
            tail.next = node; // Attaching mới nút sau hiện tại đuôi.
            tail = node; // Cập nhật đuôi tham chiếu để mới nút.
        }
        return true; // Trả về đúng để cho biết thành công đặt chỗ.
    }

    // Phương thức để hiển thị tất cả đặt chỗ trong danh sách.
    /* Bước 1: Bắt đầu từ đầu nút của đặt chỗ danh sách.
       Bước 2: Traverse mỗi nút sequentially.
       Bước 3: In đặt chỗ thông tin cho mỗi nút. */
    public void display() {
        BookingNode current = head; // Bắt đầu duyệt từ đầu.
        while (current != null) { // Tiếp tục cho đến khi đạt kết thúc của danh sách.
            System.out.println(current.info); // In hiện tại đặt chỗ thông tin.
            current = current.next; // Di chuyển để tiếp theo nút trong danh sách.
        }
    }

    // Phương thức để sort đặt chỗ bằng thuyền mã then khách hàng mã sử dụng chèn sort trên danh sách.
    /* Bước 1: Tạo một mới được sắp xếp danh sách bắt đầu như trống.
       Bước 2: Traverse hiện tại danh sách và chèn mỗi nút vào được sắp xếp danh sách trong thứ tự.
       Bước 3: Cập nhật đầu và đuôi references để được sắp xếp danh sách. */
    public void sort() {
        BookingNode sortedHead = null; // Khởi tạo đầu của được sắp xếp danh sách như rỗng.
        BookingNode current = head; // Bắt đầu duyệt của gốc danh sách.
        while (current != null) { // Tiếp tục cho đến khi tất cả nút là processed.
            BookingNode next = current.next; // Lưu tiếp theo nút trước re-linking.
            sortedHead = insertSorted(sortedHead, current); // Chèn hiện tại nút vào được sắp xếp danh sách.
            current = next; // Di chuyển để tiếp theo nút để be được chèn.
        }
        head = sortedHead; // Cập nhật đầu để được sắp xếp danh sách đầu.
        tail = head; // Đặt lại đuôi để đầu trước duyệt.
        if (tail != null) { // Kiểm tra nếu danh sách là không trống.
            while (tail.next != null) { // Traversing để kết thúc để tìm mới đuôi.
                tail = tail.next; // Di chuyển đuôi tham chiếu để tiếp theo nút.
            }
        }
    }

    // Phụ trợ phương thức để chèn một nút vào được sắp xếp danh sách.
    /* Bước 1: Nếu được sắp xếp danh sách là trống, set nút như đầu.
       Bước 2: Nếu nút nên be đặt trước hiện tại đầu, chèn tại beginning.
       Bước 3: Otherwise, traverse cho đến khi finding chèn điểm.
       Bước 4: Chèn nút và trả về được sắp xếp đầu. */
    private BookingNode insertSorted(BookingNode sortedHead, BookingNode node) {
        node.next = null; // Đảm bảo của nút tiếp theo tham chiếu là xóa sạch trước chèn.
        if (sortedHead == null) { // Kiểm tra nếu được sắp xếp danh sách là trống.
            return node; // Trả về nút như mới đầu.
        }
        if (compare(node.info, sortedHead.info) < 0) { // Kiểm tra nếu nút nên be đặt trước đầu.
            node.next = sortedHead; // Linking nút trước hiện tại đầu.
            return node; // Trả về nút như mới đầu.
        }
        BookingNode current = sortedHead; // Bắt đầu duyệt từ được sắp xếp đầu.
        while (current.next != null && compare(node.info, current.next.info) >= 0) { // Di chuyển cho đến khi đúng vị trí là tìm thấy.
            current = current.next; // Tiến để tiếp theo nút trong được sắp xếp danh sách.
        }
        node.next = current.next; // Linking nút sau hiện tại.
        current.next = node; // Cập nhật của hiện tại tiếp theo để mới nút.
        return sortedHead; // Trả về đầu của được sắp xếp danh sách.
    }

    // Phụ trợ phương thức so sánh hai đặt chỗ bản ghi bằng thuyền mã then khách hàng mã.
    /* Bước 1: So sánh thuyền mã của hai đặt chỗ.
       Bước 2: Nếu thuyền mã khác nhau, trả về so sánh kết quả.
       Bước 3: Otherwise so sánh khách hàng mã và trả về kết quả. */
    private int compare(Booking a, Booking b) {
        int cmp = a.bcode.compareTo(b.bcode); // So sánh thuyền mã của đặt chỗ.
        if (cmp != 0) { // Kiểm tra nếu thuyền mã là khác.
            return cmp; // Trả về so sánh kết quả khi thuyền mã khác nhau.
        }
        return a.ccode.compareTo(b.ccode); // So sánh khách hàng mã khi thuyền mã là bằng nhau.
    }
}

// BoatBookingSystem lớp cung cấp main mục điểm và menu-driven giao diện.
public class BoatBookingSystem {
    // Trường lưu thuyền nhị phân tìm kiếm cây thực thể.
    private BoatBST boatTree; // Cây chứa thuyền dữ liệu.
    // Trường lưu khách hàng liên kết danh sách thực thể.
    private CustomerList customerList; // Liên kết danh sách chứa khách hàng.
    // Trường lưu đặt chỗ liên kết danh sách thực thể.
    private BookingList bookingList; // Liên kết danh sách chứa đặt chỗ.
    // Trường lưu scanner cho đọc người dùng nhập.
    private Scanner scanner; // Scanner cho bảng điều khiển nhập.

    // Hàm khởi tạo khởi tạo tất cả dữ liệu cấu trúc và scanner.
    BoatBookingSystem() {
        boatTree = new BoatBST(); // Tạo một mới thuyền nhị phân tìm kiếm cây.
        customerList = new CustomerList(); // Tạo một mới khách hàng liên kết danh sách.
        bookingList = new BookingList(); // Tạo một mới đặt chỗ liên kết danh sách.
        scanner = new Scanner(System.in); // Tạo một mới Scanner cho đọc người dùng nhập.
    }

    // Phương thức bắt đầu menu vòng lặp để tương tác với người dùng.
    /* Bước 1: Hiển thị main menu tùy chọn.
       Bước 2: Đọc user's lựa chọn từ nhập.
       Bước 3: Execute corresponding action dựa trên lựa chọn.
       Bước 4: Repeat cho đến khi người dùng selects thoát. */
    public void run() {
        boolean running = true; // Cờ điều khiển menu vòng lặp.
        while (running) { // Lặp cho đến khi người dùng quyết định để thoát.
            printMenu(); // Hiển thị available menu tùy chọn.
            System.out.print("Choose an option: "); // Yêu cầu người dùng cho nhập.
            String choice = scanner.nextLine().trim(); // Đọc user's lựa chọn và cắt bỏ khoảng trắng.
            switch (choice) { // Đánh giá user's lựa chọn.
                case "1.1": // Xử lý thuyền dữ liệu tải từ tệp.
                    handleLoadBoat(); // Gọi phương thức để tải thuyền dữ liệu.
                    break; // Thoát sau xử lý lựa chọn.
                case "1.2": // Xử lý thuyền chèn.
                    handleInsertBoat(); // Gọi phương thức để chèn một mới thuyền.
                    break; // Thoát sau xử lý lựa chọn.
                case "1.3": // Xử lý trong-thứ tự duyệt hiển thị.
                    boatTree.inOrderTraversal(); // Gọi duyệt để in thuyền trong thứ tự.
                    break; // Thoát sau xử lý lựa chọn.
                case "1.4": // Xử lý breadth-đầu tiên duyệt hiển thị.
                    boatTree.breadthFirstTraversal(); // Gọi BFS duyệt để in thuyền level bằng level.
                    break; // Thoát sau xử lý lựa chọn.
                case "1.5": // Xử lý trong-thứ tự duyệt để tệp.
                    handleInOrderToFile(); // Gọi phương thức để ghi duyệt để một tệp.
                    break; // Thoát sau xử lý lựa chọn.
                case "1.6": // Xử lý tìm kiếm bằng thuyền mã.
                    handleSearchBoat(); // Gọi phương thức để tìm kiếm cho một thuyền.
                    break; // Thoát sau xử lý lựa chọn.
                case "1.7": // Xử lý xóa bằng thuyền mã.
                    handleDeleteBoat(); // Gọi phương thức để xóa một thuyền sử dụng copying.
                    break; // Thoát sau xử lý lựa chọn.
                case "1.8": // Xử lý cây balancing.
                    boatTree.balance(); // Gọi cân bằng phương thức để xây lại cây.
                    System.out.println("Boat tree balanced."); // Thông báo người dùng mà balancing là hoàn tất.
                    break; // Thoát sau xử lý lựa chọn.
                case "1.9": // Xử lý counting thuyền.
                    System.out.println("Number of boats: " + boatTree.countBoats()); // In tổng số của thuyền.
                    break; // Thoát sau xử lý lựa chọn.
                case "2.1": // Xử lý khách hàng dữ liệu tải từ tệp.
                    handleLoadCustomer(); // Gọi phương thức để tải khách hàng từ tệp.
                    break; // Thoát sau xử lý lựa chọn.
                case "2.2": // Xử lý thêm một khách hàng.
                    handleAddCustomer(); // Gọi phương thức để thêm một mới khách hàng để danh sách.
                    break; // Thoát sau xử lý lựa chọn.
                case "2.3": // Xử lý hiển thị của khách hàng.
                    customerList.display(); // Hiển thị tất cả khách hàng trong danh sách.
                    break; // Thoát sau xử lý lựa chọn.
                case "2.4": // Xử lý lưu khách hàng để tệp.
                    handleSaveCustomers(); // Gọi phương thức để save khách hàng danh sách để một tệp.
                    break; // Thoát sau xử lý lựa chọn.
                case "2.5": // Xử lý tìm kiếm cho khách hàng bằng mã.
                    handleSearchCustomer(); // Gọi phương thức để tìm kiếm cho một khách hàng.
                    break; // Thoát sau xử lý lựa chọn.
                case "2.6": // Xử lý xóa của một khách hàng bằng mã.
                    handleDeleteCustomer(); // Gọi phương thức để xóa một khách hàng.
                    break; // Thoát sau xử lý lựa chọn.
                case "3.1": // Xử lý đặt chỗ nhập.
                    handleAddBooking(); // Gọi phương thức để thêm một mới đặt chỗ.
                    break; // Thoát sau xử lý lựa chọn.
                case "3.2": // Xử lý hiển thị của đặt chỗ.
                    bookingList.display(); // Hiển thị tất cả đặt chỗ bản ghi.
                    break; // Thoát sau xử lý lựa chọn.
                case "3.3": // Xử lý sorting của đặt chỗ.
                    bookingList.sort(); // Sorting đặt chỗ danh sách bằng thuyền mã và khách hàng mã.
                    System.out.println("Bookings sorted by boat code and customer code."); // Thông báo người dùng về sorting hoàn thành.
                    break; // Thoát sau xử lý lựa chọn.
                case "0": // Xử lý thoát tùy chọn.
                    running = false; // Thiết lập cờ để sai để thoát vòng lặp.
                    System.out.println("Exiting Boat Booking System."); // Thông báo người dùng về thoát.
                    break; // Thoát sau xử lý lựa chọn.
                default: // Xử lý không hợp lệ tùy chọn.
                    System.out.println("Invalid option. Please try again."); // Thông báo người dùng về không hợp lệ nhập.
                    break; // Thoát sau xử lý lựa chọn.
            }
        }
    }

    // Phương thức hiển thị menu tùy chọn.
    /* Bước 1: In headers và categories cho clarity.
       Bước 2: In mỗi numbered tùy chọn trên bảng điều khiển. */
    private void printMenu() {
        System.out.println("\nBoat Booking System Menu"); // In menu tiêu đề với một trống dòng trước nó.
        System.out.println("Products (Boats):"); // In sản phẩm phần tiêu đề.
        System.out.println("1.1. Load data from file"); // In tùy chọn 1.1.
        System.out.println("1.2. Input & insert data"); // In tùy chọn 1.2.
        System.out.println("1.3. In-order traverse"); // In tùy chọn 1.3.
        System.out.println("1.4. Breadth-first traverse"); // In tùy chọn 1.4.
        System.out.println("1.5. In-order traverse to file"); // In tùy chọn 1.5.
        System.out.println("1.6. Search by bcode"); // In tùy chọn 1.6.
        System.out.println("1.7. Delete by bcode by copying"); // In tùy chọn 1.7.
        System.out.println("1.8. Simply balancing"); // In tùy chọn 1.8.
        System.out.println("1.9. Count number of boats"); // In tùy chọn 1.9.
        System.out.println("Customers:"); // In khách hàng phần tiêu đề.
        System.out.println("2.1. Load data from file"); // In tùy chọn 2.1.
        System.out.println("2.2. Input & add to the end"); // In tùy chọn 2.2.
        System.out.println("2.3. Display data"); // In tùy chọn 2.3.
        System.out.println("2.4. Save customer list to file"); // In tùy chọn 2.4.
        System.out.println("2.5. Search by ccode"); // In tùy chọn 2.5.
        System.out.println("2.6. Delete by ccode"); // In tùy chọn 2.6.
        System.out.println("Bookings:"); // In đặt chỗ phần tiêu đề.
        System.out.println("3.1. Input data"); // In tùy chọn 3.1.
        System.out.println("3.2. Display booking data"); // In tùy chọn 3.2.
        System.out.println("3.3. Sort by bcode + ccode"); // In tùy chọn 3.3.
        System.out.println("0. Exit"); // In thoát tùy chọn.
    }

    // Phương thức xử lý tải của thuyền dữ liệu từ một tệp.
    /* Bước 1: Nhắc người dùng để enter tệp tên.
       Bước 2: Attempt để tải thuyền từ tệp via cây.
       Bước 3: Inform người dùng về thành công hoặc thất bại. */
    private void handleLoadBoat() {
        System.out.print("Enter boat data file name: "); // Yêu cầu người dùng cho tệp tên.
        String filename = scanner.nextLine().trim(); // Đọc tệp tên từ nhập.
        try { // Bắt đầu một thử khối để xử lý Ngoại lệ IO.
            boatTree.loadFromFile(filename); // Tải thuyền từ được chỉ định tệp.
            System.out.println("Boat data loaded successfully."); // Thông báo người dùng về thành công.
        } catch (IOException | NumberFormatException e) { // Bắt Ngoại lệ IO và số format lỗi.
            System.out.println("Failed to load boat data: " + e.getMessage()); // Thông báo người dùng về thất bại với lỗi thông báo.
        }
    }

    // Phương thức xử lý chèn của một mới thuyền via người dùng nhập.
    /* Bước 1: Nhắc người dùng cho mỗi thuyền attribute.
       Bước 2: Validate số fields sử dụng phân tích.
       Bước 3: Call cây chèn phương thức.
       Bước 4: Inform người dùng về thành công hoặc trùng mã. */
    private void handleInsertBoat() {
        try { // Bắt đầu một thử khối để catch không hợp lệ số định dạng.
            System.out.print("Enter boat code: "); // Yêu cầu cho thuyền mã.
            String bcode = scanner.nextLine().trim(); // Đọc thuyền mã.
            System.out.print("Enter boat name: "); // Yêu cầu cho thuyền tên.
            String name = scanner.nextLine().trim(); // Đọc thuyền tên.
            System.out.print("Enter seat count: "); // Yêu cầu cho ghế đếm.
            int seat = Integer.parseInt(scanner.nextLine().trim()); // Phân tích ghế đếm.
            System.out.print("Enter booked seats: "); // Yêu cầu cho đã đặt ghế.
            int booked = Integer.parseInt(scanner.nextLine().trim()); // Phân tích đã đặt ghế.
            System.out.print("Enter departure place: "); // Yêu cầu cho khởi hành địa điểm.
            String depart = scanner.nextLine().trim(); // Đọc khởi hành địa điểm.
            System.out.print("Enter rate: "); // Yêu cầu cho đánh giá.
            double rate = Double.parseDouble(scanner.nextLine().trim()); // Phân tích đánh giá.
            if (seat <= 0 || booked < 0 || booked > seat || rate <= 0) { // Xác thực số constraints.
                System.out.println("Invalid numeric values for seat/booked/rate."); // Thông báo người dùng về không hợp lệ nhập.
                return; // Thoát phương thức without chèn.
            }
            boolean inserted = boatTree.insert(bcode, name, seat, booked, depart, rate); // Đang thử để chèn thuyền.
            if (inserted) { // Kiểm tra nếu chèn thành công.
                System.out.println("Boat inserted successfully."); // Thông báo người dùng về thành công.
            } else { // Xử lý trùng mã trường hợp.
                System.out.println("Boat code already exists. Insertion failed."); // Thông báo người dùng về thất bại.
            }
        } catch (NumberFormatException e) { // Bắt không hợp lệ số định dạng.
            System.out.println("Invalid number format: " + e.getMessage()); // Thông báo người dùng về lỗi.
        }
    }

    // Phương thức xử lý ghi trong-thứ tự duyệt để một tệp.
    /* Bước 1: Nhắc người dùng cho đầu ra tệp tên.
       Bước 2: Execute tree's trong-thứ tự để tệp phương thức.
       Bước 3: Inform người dùng về thành công hoặc thất bại. */
    private void handleInOrderToFile() {
        System.out.print("Enter output file name: "); // Yêu cầu người dùng cho tệp tên.
        String filename = scanner.nextLine().trim(); // Đọc tệp tên từ nhập.
        try { // Bắt đầu thử khối để xử lý Ngoại lệ IO.
            boatTree.inOrderToFile(filename); // Ghi cây dữ liệu để tệp.
            System.out.println("Boat data written to file successfully."); // Thông báo người dùng về thành công.
        } catch (IOException e) { // Bắt Ngoại lệ IO từ ghi.
            System.out.println("Failed to write to file: " + e.getMessage()); // Thông báo người dùng về thất bại.
        }
    }

    // Phương thức xử lý tìm kiếm cho một thuyền bằng mã.
    /* Bước 1: Nhắc người dùng cho thuyền mã.
       Bước 2: Use cây tìm kiếm phương thức để tìm thuyền.
       Bước 3: Hiển thị kết quả hoặc notify khi không tìm thấy. */
    private void handleSearchBoat() {
        System.out.print("Enter boat code to search: "); // Yêu cầu cho thuyền mã.
        String bcode = scanner.nextLine().trim(); // Đọc thuyền mã từ nhập.
        BoatNode node = boatTree.search(bcode); // Tìm kiếm cho thuyền trong cây.
        if (node != null) { // Kiểm tra nếu thuyền đã tìm thấy.
            System.out.println("Boat found: " + node.info); // Hiển thị thuyền thông tin.
        } else { // Xử lý trường hợp nơi thuyền là không có.
            System.out.println("Boat not found."); // Thông báo người dùng mà thuyền không không tồn tại.
        }
    }

    // Phương thức xử lý xóa của một thuyền bằng mã sử dụng sao chép technique.
    /* Bước 1: Nhắc người dùng cho thuyền mã để xóa.
       Bước 2: Call deleteByCopying phương thức trên cây.
       Bước 3: Inform người dùng về thành công hoặc thất bại. */
    private void handleDeleteBoat() {
        System.out.print("Enter boat code to delete: "); // Yêu cầu cho thuyền mã.
        String bcode = scanner.nextLine().trim(); // Đọc thuyền mã từ nhập.
        boolean deleted = boatTree.deleteByCopying(bcode); // Đang thử để xóa thuyền.
        if (deleted) { // Kiểm tra nếu xóa thành công.
            System.out.println("Boat deleted successfully."); // Thông báo người dùng về thành công.
        } else { // Xử lý trường hợp nơi xóa thất bại.
            System.out.println("Boat not found or deletion failed."); // Thông báo người dùng về thất bại.
        }
    }

    // Phương thức xử lý tải khách hàng từ một tệp.
    /* Bước 1: Nhắc người dùng cho khách hàng tệp tên.
       Bước 2: Tải khách hàng sử dụng liên kết danh sách loader.
       Bước 3: Inform người dùng về outcome. */
    private void handleLoadCustomer() {
        System.out.print("Enter customer data file name: "); // Yêu cầu người dùng cho tệp tên.
        String filename = scanner.nextLine().trim(); // Đọc tệp tên từ nhập.
        try { // Bắt đầu thử khối để xử lý ngoại lệ.
            customerList.loadFromFile(filename); // Tải khách hàng từ tệp.
            System.out.println("Customer data loaded successfully."); // Thông báo người dùng về thành công.
        } catch (IOException e) { // Bắt Ngoại lệ IO trong khi tải.
            System.out.println("Failed to load customer data: " + e.getMessage()); // Thông báo người dùng về thất bại.
        }
    }

    // Phương thức xử lý bổ sung của một mới khách hàng via nhập.
    /* Bước 1: Nhắc cho khách hàng thuộc tính.
       Bước 2: Validate điện thoại số để contain chữ số chỉ.
       Bước 3: Thêm khách hàng để kết thúc của danh sách.
       Bước 4: Inform người dùng về thành công hoặc trùng mã. */
    private void handleAddCustomer() {
        System.out.print("Enter customer code: "); // Yêu cầu cho khách hàng mã.
        String ccode = scanner.nextLine().trim(); // Đọc khách hàng mã từ nhập.
        System.out.print("Enter customer name: "); // Yêu cầu cho khách hàng tên.
        String name = scanner.nextLine().trim(); // Đọc khách hàng tên từ nhập.
        System.out.print("Enter phone number: "); // Yêu cầu cho điện thoại số.
        String phone = scanner.nextLine().trim(); // Đọc điện thoại số từ nhập.
        if (!phone.matches("\\d+")) { // Xác thực mà điện thoại chứa chữ số chỉ.
            System.out.println("Phone number must contain digits only."); // Thông báo người dùng về không hợp lệ điện thoại số.
            return; // Thoát phương thức without thêm khách hàng.
        }
        boolean added = customerList.addLast(ccode, name, phone); // Đang thử để thêm khách hàng để danh sách.
        if (added) { // Kiểm tra nếu bổ sung thành công.
            System.out.println("Customer added successfully."); // Thông báo người dùng về thành công.
        } else { // Xử lý trùng mã trường hợp.
            System.out.println("Customer code already exists."); // Thông báo người dùng về thất bại.
        }
    }

    // Phương thức xử lý lưu khách hàng để một tệp.
    /* Bước 1: Nhắc người dùng cho đầu ra tệp tên.
       Bước 2: Invoke liên kết danh sách save phương thức.
       Bước 3: Inform người dùng về thành công hoặc thất bại. */
    private void handleSaveCustomers() {
        System.out.print("Enter output file name: "); // Yêu cầu cho tệp tên.
        String filename = scanner.nextLine().trim(); // Đọc tệp tên từ nhập.
        try { // Bắt đầu thử khối để xử lý Ngoại lệ IO.
            customerList.saveToFile(filename); // Lưu khách hàng dữ liệu để tệp.
            System.out.println("Customer data saved successfully."); // Thông báo người dùng về thành công.
        } catch (IOException e) { // Bắt Ngoại lệ IO từ ghi.
            System.out.println("Failed to save customer data: " + e.getMessage()); // Thông báo người dùng về thất bại.
        }
    }

    // Phương thức xử lý tìm kiếm cho một khách hàng bằng mã.
    /* Bước 1: Nhắc người dùng cho khách hàng mã.
       Bước 2: Tìm kiếm danh sách sử dụng findByCode.
       Bước 3: Hiển thị kết quả hoặc notify khi không có. */
    private void handleSearchCustomer() {
        System.out.print("Enter customer code to search: "); // Yêu cầu cho khách hàng mã.
        String ccode = scanner.nextLine().trim(); // Đọc khách hàng mã từ nhập.
        CustomerNode node = customerList.findByCode(ccode); // Tìm kiếm cho khách hàng trong danh sách.
        if (node != null) { // Kiểm tra nếu khách hàng đã tìm thấy.
            System.out.println("Customer found: " + node.info); // Hiển thị khách hàng thông tin.
        } else { // Xử lý trường hợp nơi khách hàng là không có.
            System.out.println("Customer not found."); // Thông báo người dùng về thiếu.
        }
    }

    // Phương thức xử lý xóa của một khách hàng bằng mã.
    /* Bước 1: Nhắc người dùng cho khách hàng mã để xóa.
       Bước 2: Invoke deleteByCode trên danh sách.
       Bước 3: Inform người dùng về thành công hoặc thất bại. */
    private void handleDeleteCustomer() {
        System.out.print("Enter customer code to delete: "); // Yêu cầu cho khách hàng mã.
        String ccode = scanner.nextLine().trim(); // Đọc khách hàng mã từ nhập.
        boolean deleted = customerList.deleteByCode(ccode); // Đang thử để xóa khách hàng.
        if (deleted) { // Kiểm tra nếu xóa thành công.
            System.out.println("Customer deleted successfully."); // Thông báo người dùng về thành công.
        } else { // Xử lý trường hợp nơi xóa thất bại.
            System.out.println("Customer not found or deletion failed."); // Thông báo người dùng về thất bại.
        }
    }

    // Phương thức xử lý bổ sung của một mới đặt chỗ.
    /* Bước 1: Nhắc cho thuyền mã, khách hàng mã, và ghế đếm.
       Bước 2: Attempt để thêm đặt chỗ while xác thực references.
       Bước 3: Inform người dùng về thành công hoặc thất bại. */
    private void handleAddBooking() {
        try { // Bắt đầu thử khối để catch số format vấn đề.
            System.out.print("Enter boat code: "); // Yêu cầu cho thuyền mã.
            String bcode = scanner.nextLine().trim(); // Đọc thuyền mã từ nhập.
            System.out.print("Enter customer code: "); // Yêu cầu cho khách hàng mã.
            String ccode = scanner.nextLine().trim(); // Đọc khách hàng mã từ nhập.
            System.out.print("Enter seat count: "); // Yêu cầu cho ghế đếm.
            int seat = Integer.parseInt(scanner.nextLine().trim()); // Phân tích ghế đếm từ nhập.
            boolean added = bookingList.addBooking(boatTree, customerList, bcode, ccode, seat); // Đang thử để thêm đặt chỗ.
            if (added) { // Kiểm tra nếu đặt chỗ thành công.
                System.out.println("Booking created successfully."); // Thông báo người dùng về thành công.
            } else { // Xử lý trường hợp nơi đặt chỗ thất bại.
                System.out.println("Booking failed. Check codes and seat availability."); // Thông báo người dùng về thất bại.
            }
        } catch (NumberFormatException e) { // Bắt không hợp lệ số định dạng.
            System.out.println("Invalid number format: " + e.getMessage()); // Thông báo người dùng về lỗi.
        }
    }

    // Main phương thức khởi chạy Thuyền Đặt chỗ Hệ thống.
    /* Bước 1: Tạo một mới BoatBookingSystem thực thể.
       Bước 2: Call run phương thức để bắt đầu menu vòng lặp. */
    public static void main(String[] args) {
        BoatBookingSystem system = new BoatBookingSystem(); // Tạo một mới thực thể của hệ thống.
        system.run(); // Bắt đầu menu-driven giao diện.
    }
}