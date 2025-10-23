package graph; // Khai báo package cho lớp GraphDemo

import java.util.List; // Sử dụng List để lưu kết quả duyệt và đường đi

public class GraphDemo { // Lớp GraphDemo chứa hàm main minh họa cách dùng các lớp đã xây
    public static void main(String[] args) { // Hàm main là điểm bắt đầu của chương trình Java
        RoadGraph graph = new RoadGraph(); // Khởi tạo đồ thị đường đi có trọng số

        City hanoi = graph.addCity("Ha Noi"); // Thêm thành phố Hà Nội vào đồ thị
        City haiphong = graph.addCity("Hai Phong"); // Thêm thành phố Hải Phòng vào đồ thị
        City danang = graph.addCity("Da Nang"); // Thêm thành phố Đà Nẵng vào đồ thị
        City hcm = graph.addCity("Ho Chi Minh"); // Thêm thành phố Hồ Chí Minh vào đồ thị
        City cantho = graph.addCity("Can Tho"); // Thêm thành phố Cần Thơ vào đồ thị

        graph.addRoad(hanoi, haiphong, 120); // Nối Hà Nội với Hải Phòng bằng quãng đường 120 km
        graph.addRoad(hanoi, danang, 780); // Nối Hà Nội với Đà Nẵng bằng quãng đường 780 km
        graph.addRoad(haiphong, danang, 820); // Nối Hải Phòng với Đà Nẵng để tạo chu trình
        graph.addRoad(danang, hcm, 960); // Nối Đà Nẵng với Hồ Chí Minh bằng quãng đường 960 km
        graph.addRoad(hcm, cantho, 170); // Nối Hồ Chí Minh với Cần Thơ bằng quãng đường 170 km
        graph.addRoad(haiphong, hcm, 1700); // Thêm đường dài giữa Hải Phòng và Hồ Chí Minh để tăng kết nối

        System.out.println("Do thi lien thong: " + graph.isConnected()); // In ra trạng thái liên thông của đồ thị
        System.out.println("Do thi co chu trinh: " + graph.hasCycle()); // In ra kết quả kiểm tra chu trình
        System.out.println("Do thi co chu trinh le: " + graph.hasOddCycle()); // In ra kết quả kiểm tra chu trình độ dài lẻ
        System.out.println("Do thi co chu trinh Euler: " + graph.hasEulerianCycle()); // In ra khả năng tồn tại chu trình Euler
        System.out.println("Do thi co chu trinh Hamilton: " + graph.hasHamiltonianCycle()); // In ra khả năng tồn tại chu trình Hamilton
        System.out.println("Do thi co chu trinh am: " + graph.hasNegativeCycle()); // In ra kết quả phát hiện chu trình âm

        List<City> bfsOrder = graph.breadthFirstTraversal(hanoi); // Thực hiện BFS từ Hà Nội
        System.out.println("Thu tu BFS: " + bfsOrder); // In ra thứ tự duyệt BFS

        List<City> dfsOrder = graph.depthFirstTraversal(hanoi); // Thực hiện DFS từ Hà Nội
        System.out.println("Thu tu DFS: " + dfsOrder); // In ra thứ tự duyệt DFS

        List<City> unweightedPath = graph.shortestPath(hanoi, cantho); // Tìm đường đi ngắn nhất theo BFS không trọng số
        System.out.println("Duong di khong trong so: " + unweightedPath); // In đường đi không trọng số

        List<City> weightedPath = graph.shortestPathDijkstra(hanoi, cantho); // Tìm đường đi ngắn nhất có trọng số bằng Dijkstra
        System.out.println("Duong di Dijkstra: " + weightedPath); // In đường đi tìm được bằng Dijkstra

        int[][] allPairs = graph.floydWarshall(); // Tính khoảng cách giữa mọi cặp thành phố bằng Floyd-Warshall
        int inf = graph.getInfinityValue(); // Lấy giá trị vô cực để hiển thị đẹp
        System.out.println("Ma tran Floyd-Warshall:"); // In tiêu đề cho ma trận
        for (int i = 0; i < allPairs.length; i++) { // Duyệt từng dòng trong ma trận khoảng cách
            for (int j = 0; j < allPairs[i].length; j++) { // Duyệt từng cột trong ma trận khoảng cách
                int value = allPairs[i][j]; // Lấy giá trị khoảng cách hiện tại
                if (value >= inf) { // Nếu giá trị bằng hoặc vượt vô cực
                    System.out.print("INF\t"); // In chữ INF để dễ nhìn
                } else { // Ngược lại là khoảng cách hữu hạn
                    System.out.print(value + "\t"); // In ra số khoảng cách thực tế
                }
            }
            System.out.println(); // Xuống dòng sau mỗi hàng của ma trận
        }

        int[] bellmanDistances = graph.bellmanFord(hanoi); // Tính khoảng cách từ Hà Nội đến các thành phố bằng Bellman-Ford
        System.out.println("Khoang cach Bellman-Ford tu Ha Noi:"); // In tiêu đề cho danh sách khoảng cách
        List<City> allCities = graph.getCities(); // Lấy danh sách thành phố để ghép với khoảng cách
        for (int i = 0; i < allCities.size(); i++) { // Duyệt qua từng thành phố
            System.out.println(allCities.get(i).getName() + ": " + bellmanDistances[i]); // In tên thành phố và khoảng cách tương ứng
        }
    }
}
