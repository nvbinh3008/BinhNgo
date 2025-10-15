package graph; // Khai báo package cho lớp RoadGraph

import java.util.ArrayDeque; // Sử dụng ArrayDeque cho hàng đợi duyệt BFS và kiểm tra liên thông
import java.util.ArrayList; // Sử dụng ArrayList cho các danh sách động lưu thành phố và cạnh
import java.util.Arrays; // Sử dụng Arrays để gán nhanh giá trị cho mảng
import java.util.Collections; // Sử dụng Collections để đảo danh sách khi dựng đường đi
import java.util.Deque; // Sử dụng Deque cho ngăn xếp trong DFS
import java.util.HashSet; // Sử dụng HashSet để đánh dấu các đỉnh đã thăm
import java.util.LinkedList; // Sử dụng LinkedList để dựng lại đường đi không trọng số
import java.util.List; // Sử dụng List làm kiểu danh sách tổng quát
import java.util.PriorityQueue; // Sử dụng PriorityQueue cho thuật toán Dijkstra
import java.util.Queue; // Sử dụng Queue cho thuật toán BFS
import java.util.Set; // Sử dụng Set để lưu các đỉnh đã duyệt

public class RoadGraph { // Lớp RoadGraph quản lý đồ thị đường đi giữa các thành phố
    private static final int INF = 1_000_000_000; // Giá trị lớn dùng làm vô cực trong các thuật toán đường đi
    private final List<City> cities; // Danh sách các thành phố hiện có
    private final List<List<Integer>> adjacencyList; // Danh sách kề lưu cạnh giữa các thành phố theo chỉ số
    private final List<List<Integer>> weightList; // Danh sách song song lưu trọng số từng cạnh

    public RoadGraph() { // Hàm tạo mặc định của đồ thị
        this.cities = new ArrayList<>(); // Khởi tạo danh sách thành phố rỗng
        this.adjacencyList = new ArrayList<>(); // Khởi tạo danh sách kề rỗng
        this.weightList = new ArrayList<>(); // Khởi tạo danh sách trọng số rỗng tương ứng
    }

    public City addCity(String name) { // Thêm một thành phố mới vào đồ thị
        City newCity = new City(cities.size(), name); // Tạo đối tượng City với mã số tăng dần
        cities.add(newCity); // Lưu thành phố mới vào danh sách cities
        adjacencyList.add(new ArrayList<>()); // Tạo danh sách kề rỗng tương ứng cho thành phố mới
        weightList.add(new ArrayList<>()); // Tạo danh sách trọng số rỗng cho thành phố mới
        return newCity; // Trả về thành phố vừa tạo để người dùng tiện sử dụng
    }

    public void addRoad(City from, City to) { // Thêm đường đi hai chiều không trọng số rõ ràng
        addRoad(from, to, 1); // Gọi tới phương thức thêm đường có trọng số với giá trị mặc định bằng 1
    }

    public void addRoad(City from, City to, int distance) { // Thêm đường đi hai chiều với trọng số cụ thể
        int fromId = from.getId(); // Lấy mã số của thành phố nguồn
        int toId = to.getId(); // Lấy mã số của thành phố đích
        adjacencyList.get(fromId).add(toId); // Thêm chỉ số thành phố đích vào danh sách kề của thành phố nguồn
        weightList.get(fromId).add(distance); // Thêm trọng số cạnh tương ứng vào danh sách trọng số của thành phố nguồn
        adjacencyList.get(toId).add(fromId); // Thêm chiều ngược lại vì đồ thị vô hướng
        weightList.get(toId).add(distance); // Thêm trọng số cho chiều ngược lại
    }

    public List<City> getNeighbors(City city) { // Lấy danh sách thành phố kề với một thành phố đã cho
        List<City> neighbors = new ArrayList<>(); // Tạo danh sách kết quả rỗng ban đầu
        for (int neighborId : adjacencyList.get(city.getId())) { // Duyệt qua từng chỉ số thành phố kề
            neighbors.add(cities.get(neighborId)); // Tra chỉ số sang đối tượng City và thêm vào kết quả
        }
        return neighbors; // Trả về danh sách thành phố kề
    }

    public boolean isConnected() { // Kiểm tra đồ thị có liên thông hay không
        if (cities.isEmpty()) { // Nếu không có thành phố nào trong đồ thị
            return true; // Trả về true vì đồ thị rỗng được xem là liên thông
        }
        Set<Integer> visited = new HashSet<>(); // Tập đánh dấu các thành phố đã ghé qua
        Queue<Integer> queue = new ArrayDeque<>(); // Hàng đợi phục vụ cho BFS kiểm tra liên thông
        visited.add(0); // Đánh dấu thành phố đầu tiên trong danh sách
        queue.add(0); // Đưa thành phố đầu tiên vào hàng đợi
        while (!queue.isEmpty()) { // Lặp cho đến khi không còn thành phố trong hàng đợi
            int currentId = queue.remove(); // Lấy mã thành phố ở đầu hàng đợi ra xử lý
            for (int neighborId : adjacencyList.get(currentId)) { // Duyệt qua từng thành phố kề với thành phố hiện tại
                if (!visited.contains(neighborId)) { // Chỉ xử lý nếu thành phố kề chưa được ghé
                    visited.add(neighborId); // Đánh dấu đã ghé để không xử lý lại
                    queue.add(neighborId); // Thêm thành phố kề vào hàng đợi để duyệt tiếp
                }
            }
        }
        return visited.size() == cities.size(); // Đồ thị liên thông khi số thành phố đã ghé bằng tổng số thành phố
    }

    public boolean hasCycle() { // Kiểm tra đồ thị vô hướng có chu trình hay không
        Set<Integer> visited = new HashSet<>(); // Tập các thành phố đã duyệt
        for (int cityId = 0; cityId < cities.size(); cityId++) { // Duyệt qua từng thành phố theo chỉ số
            if (!visited.contains(cityId)) { // Nếu thành phố chưa được duyệt
                if (detectCycleFrom(cityId, -1, visited)) { // Gọi DFS và trả về true nếu phát hiện chu trình
                    return true; // Kết luận tồn tại chu trình
                }
            }
        }
        return false; // Trả về false nếu duyệt xong mà không tìm thấy chu trình nào
    }

    private boolean detectCycleFrom(int currentId, int parentId, Set<Integer> visited) { // Hàm DFS phụ để phát hiện chu trình
        visited.add(currentId); // Đánh dấu thành phố hiện tại đã được duyệt
        for (int neighborId : adjacencyList.get(currentId)) { // Duyệt qua các thành phố kề với thành phố hiện tại
            if (!visited.contains(neighborId)) { // Nếu gặp thành phố chưa duyệt
                if (detectCycleFrom(neighborId, currentId, visited)) { // Đệ quy kiểm tra các nhánh sâu hơn
                    return true; // Nếu nhánh con phát hiện chu trình thì trả về true
                }
            } else if (neighborId != parentId) { // Nếu gặp lại thành phố đã duyệt và không phải cha trực tiếp
                return true; // Phát hiện chu trình vì có cạnh ngược lại ngoài cha
            }
        }
        return false; // Không phát hiện chu trình từ nhánh hiện tại
    }

    public List<City> breadthFirstTraversal(City start) { // Thuật toán duyệt BFS bắt đầu từ một thành phố
        List<City> order = new ArrayList<>(); // Danh sách lưu thứ tự các thành phố được duyệt
        Set<Integer> visited = new HashSet<>(); // Tập đánh dấu các thành phố đã ghé qua
        Queue<Integer> queue = new ArrayDeque<>(); // Hàng đợi phục vụ cho BFS
        visited.add(start.getId()); // Đánh dấu thành phố bắt đầu
        queue.add(start.getId()); // Đưa thành phố bắt đầu vào hàng đợi
        while (!queue.isEmpty()) { // Lặp cho đến khi không còn thành phố trong hàng đợi
            int currentId = queue.remove(); // Lấy mã thành phố ở đầu hàng đợi ra xử lý
            City currentCity = cities.get(currentId); // Lấy đối tượng City tương ứng với mã vừa lấy
            order.add(currentCity); // Ghi nhận thành phố vào kết quả
            for (int neighborId : adjacencyList.get(currentId)) { // Duyệt qua từng thành phố kề với thành phố hiện tại
                if (!visited.contains(neighborId)) { // Chỉ xử lý nếu thành phố kề chưa được ghé
                    visited.add(neighborId); // Đánh dấu đã ghé để không xử lý lại
                    queue.add(neighborId); // Thêm thành phố kề vào hàng đợi để duyệt tiếp
                }
            }
        }
        return order; // Hoàn trả thứ tự duyệt BFS để người học dễ quan sát
    }

    public List<City> depthFirstTraversal(City start) { // Thuật toán duyệt DFS dùng ngăn xếp
        List<City> order = new ArrayList<>(); // Danh sách lưu thứ tự các thành phố đã duyệt
        Set<Integer> visited = new HashSet<>(); // Tập đánh dấu các thành phố đã ghé
        Deque<Integer> stack = new ArrayDeque<>(); // Ngăn xếp dùng để mô phỏng DFS
        stack.push(start.getId()); // Đưa thành phố bắt đầu vào ngăn xếp
        while (!stack.isEmpty()) { // Lặp cho đến khi không còn thành phố trong ngăn xếp
            int currentId = stack.pop(); // Lấy mã thành phố ở đỉnh ngăn xếp ra xử lý
            if (visited.contains(currentId)) { // Bỏ qua nếu thành phố đã được duyệt trước đó
                continue; // Chuyển sang bước tiếp theo
            }
            visited.add(currentId); // Đánh dấu thành phố hiện tại
            City currentCity = cities.get(currentId); // Lấy đối tượng City tương ứng
            order.add(currentCity); // Ghi nhận thành phố vào kết quả
            for (int neighborId : adjacencyList.get(currentId)) { // Duyệt qua từng thành phố kề
                if (!visited.contains(neighborId)) { // Chỉ thêm vào ngăn xếp nếu chưa duyệt
                    stack.push(neighborId); // Đưa thành phố kề vào ngăn xếp để xử lý sau
                }
            }
        }
        return order; // Trả về thứ tự duyệt DFS để tiện đối chiếu với BFS
    }

    public List<City> shortestPath(City start, City end) { // Tìm đường đi ngắn nhất bằng BFS trên đồ thị vô hướng không trọng số
        int startId = start.getId(); // Lấy mã của thành phố bắt đầu
        int endId = end.getId(); // Lấy mã của thành phố đích
        int[] previous = new int[cities.size()]; // Mảng lưu lại đỉnh trước đó trên đường đi
        for (int i = 0; i < previous.length; i++) { // Khởi tạo giá trị ban đầu cho mảng previous
            previous[i] = -1; // Gán -1 để thể hiện chưa có đỉnh trước đó
        }
        Queue<Integer> queue = new LinkedList<>(); // Hàng đợi sử dụng trong BFS
        Set<Integer> visited = new HashSet<>(); // Tập đánh dấu các thành phố đã thăm
        visited.add(startId); // Đánh dấu thành phố bắt đầu
        queue.add(startId); // Thêm thành phố bắt đầu vào hàng đợi
        while (!queue.isEmpty()) { // Lặp cho đến khi hàng đợi rỗng
            int currentId = queue.remove(); // Lấy một thành phố từ hàng đợi ra
            if (currentId == endId) { // Nếu đã đến thành phố đích
                break; // Thoát vòng lặp vì đã tìm thấy đường đi
            }
            for (int neighborId : adjacencyList.get(currentId)) { // Duyệt qua các thành phố kề
                if (!visited.contains(neighborId)) { // Chỉ xử lý khi thành phố kề chưa ghé
                    visited.add(neighborId); // Đánh dấu đã ghé để tránh lặp
                    previous[neighborId] = currentId; // Ghi nhận đỉnh trước đó của thành phố kề
                    queue.add(neighborId); // Thêm thành phố kề vào hàng đợi để tiếp tục BFS
                }
            }
        }
        if (previous[endId] == -1 && startId != endId) { // Nếu không có đường đi và điểm đầu khác điểm cuối
            return Collections.emptyList(); // Trả về danh sách rỗng biểu thị không tìm thấy đường đi
        }
        List<City> path = new ArrayList<>(); // Danh sách lưu đường đi ngắn nhất từ cuối lên đầu
        for (int currentId = endId; currentId != -1; currentId = previous[currentId]) { // Lần ngược từ đích về nguồn
            path.add(cities.get(currentId)); // Thêm thành phố hiện tại vào danh sách đường đi
        }
        Collections.reverse(path); // Đảo ngược danh sách để có thứ tự từ nguồn đến đích
        return path; // Trả về đường đi ngắn nhất để người học quan sát
    }

    public List<City> shortestPathDijkstra(City start, City end) { // Tìm đường đi ngắn nhất có trọng số bằng thuật toán Dijkstra
        int n = cities.size(); // Lấy số lượng thành phố trong đồ thị
        int[] distance = new int[n]; // Mảng lưu khoảng cách ngắn nhất tạm thời tới từng thành phố
        int[] previous = new int[n]; // Mảng lưu lại đỉnh trước đó trên đường đi tốt nhất hiện tại
        Arrays.fill(distance, INF); // Khởi tạo tất cả khoảng cách bằng vô cực
        Arrays.fill(previous, -1); // Khởi tạo đỉnh trước đó bằng -1 thể hiện chưa xác định
        int startId = start.getId(); // Lấy mã của thành phố bắt đầu
        int endId = end.getId(); // Lấy mã của thành phố đích
        distance[startId] = 0; // Khoảng cách tới chính nó bằng 0
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1])); // Hàng đợi ưu tiên chọn đỉnh có khoảng cách nhỏ nhất
        queue.add(new int[]{startId, 0}); // Đưa thành phố bắt đầu cùng khoảng cách 0 vào hàng đợi
        while (!queue.isEmpty()) { // Lặp cho đến khi không còn đỉnh nào cần xử lý
            int[] entry = queue.remove(); // Lấy phần tử có khoảng cách nhỏ nhất ra khỏi hàng đợi
            int currentId = entry[0]; // Tách mã thành phố đang xét
            int currentDistance = entry[1]; // Lấy khoảng cách tương ứng của thành phố đó
            if (currentDistance > distance[currentId]) { // Nếu phần tử đang xét không còn tối ưu
                continue; // Bỏ qua để tránh xử lý thừa
            }
            if (currentId == endId) { // Nếu đã tới thành phố đích
                break; // Thoát vòng lặp vì đã có khoảng cách tốt nhất
            }
            List<Integer> neighbors = adjacencyList.get(currentId); // Lấy danh sách hàng xóm của thành phố hiện tại
            List<Integer> weights = weightList.get(currentId); // Lấy danh sách trọng số tương ứng với từng hàng xóm
            for (int i = 0; i < neighbors.size(); i++) { // Duyệt qua tất cả hàng xóm
                int neighborId = neighbors.get(i); // Lấy mã hàng xóm
                int weight = weights.get(i); // Lấy trọng số cạnh tới hàng xóm đó
                int newDistance = currentDistance + weight; // Tính khoảng cách mới nếu đi qua thành phố hiện tại
                if (newDistance < distance[neighborId]) { // Nếu khoảng cách mới tốt hơn khoảng cách cũ
                    distance[neighborId] = newDistance; // Cập nhật khoảng cách tốt hơn
                    previous[neighborId] = currentId; // Lưu lại đỉnh trước đó để dựng đường đi
                    queue.add(new int[]{neighborId, newDistance}); // Đưa hàng xóm vào hàng đợi để xét tiếp
                }
            }
        }
        if (distance[endId] == INF) { // Nếu không tìm được đường đi tới thành phố đích
            return Collections.emptyList(); // Trả về danh sách rỗng để biểu thị thất bại
        }
        List<City> path = new ArrayList<>(); // Danh sách lưu đường đi tìm được
        for (int currentId = endId; currentId != -1; currentId = previous[currentId]) { // Lần ngược từ đích về nguồn
            path.add(cities.get(currentId)); // Thêm thành phố hiện tại vào danh sách đường đi
        }
        Collections.reverse(path); // Đảo ngược để có đường đi theo chiều từ nguồn tới đích
        return path; // Trả về đường đi có trọng số ngắn nhất
    }

    public int[][] floydWarshall() { // Thuật toán Floyd-Warshall để tìm đường đi ngắn nhất giữa mọi cặp thành phố
        int n = cities.size(); // Lấy số lượng thành phố trong đồ thị
        int[][] dist = new int[n][n]; // Ma trận lưu khoảng cách giữa mọi cặp thành phố
        for (int i = 0; i < n; i++) { // Duyệt qua từng hàng của ma trận
            for (int j = 0; j < n; j++) { // Duyệt qua từng cột của ma trận
                if (i == j) { // Nếu đang xét phần tử trên đường chéo chính
                    dist[i][j] = 0; // Khoảng cách từ một thành phố tới chính nó bằng 0
                } else { // Ngược lại là hai thành phố khác nhau
                    dist[i][j] = INF; // Gán khoảng cách ban đầu bằng vô cực
                }
            }
        }
        for (int i = 0; i < n; i++) { // Duyệt qua từng thành phố để đặt khoảng cách trực tiếp
            List<Integer> neighbors = adjacencyList.get(i); // Lấy danh sách hàng xóm của thành phố i
            List<Integer> weights = weightList.get(i); // Lấy trọng số tương ứng với từng hàng xóm
            for (int j = 0; j < neighbors.size(); j++) { // Duyệt qua các hàng xóm
                int neighborId = neighbors.get(j); // Lấy mã hàng xóm
                int weight = weights.get(j); // Lấy trọng số cạnh
                dist[i][neighborId] = Math.min(dist[i][neighborId], weight); // Cập nhật khoảng cách trực tiếp nhỏ nhất
            }
        }
        for (int k = 0; k < n; k++) { // Duyệt qua từng thành phố làm trung gian
            for (int i = 0; i < n; i++) { // Duyệt qua thành phố nguồn
                for (int j = 0; j < n; j++) { // Duyệt qua thành phố đích
                    if (dist[i][k] == INF || dist[k][j] == INF) { // Bỏ qua nếu không có đường đi qua trung gian
                        continue; // Tiếp tục sang cặp khác
                    }
                    int newDistance = dist[i][k] + dist[k][j]; // Tính khoảng cách thông qua trung gian k
                    if (newDistance < dist[i][j]) { // Nếu khoảng cách mới nhỏ hơn khoảng cách hiện tại
                        dist[i][j] = newDistance; // Cập nhật khoảng cách tốt hơn cho cặp (i, j)
                    }
                }
            }
        }
        return dist; // Trả về ma trận khoảng cách giữa mọi cặp thành phố
    }

    public int[] bellmanFord(City start) { // Thuật toán Bellman-Ford tìm đường đi ngắn nhất từ một nguồn
        int n = cities.size(); // Lấy số lượng thành phố trong đồ thị
        int[] distance = new int[n]; // Mảng lưu khoảng cách tốt nhất tới từng thành phố
        Arrays.fill(distance, INF); // Khởi tạo khoảng cách bằng vô cực
        int startId = start.getId(); // Lấy mã số thành phố nguồn
        distance[startId] = 0; // Khoảng cách từ nguồn tới chính nó bằng 0
        for (int iteration = 0; iteration < n - 1; iteration++) { // Lặp n-1 lần để thư giãn các cạnh
            boolean updated = false; // Cờ đánh dấu có cập nhật khoảng cách trong vòng lặp hay không
            for (int fromId = 0; fromId < n; fromId++) { // Duyệt qua từng thành phố làm điểm xuất phát
                if (distance[fromId] == INF) { // Nếu chưa thể đi tới thành phố này
                    continue; // Bỏ qua vì không thể thư giãn cạnh xuất phát từ đây
                }
                List<Integer> neighbors = adjacencyList.get(fromId); // Lấy danh sách hàng xóm của fromId
                List<Integer> weights = weightList.get(fromId); // Lấy trọng số tương ứng
                for (int edgeIndex = 0; edgeIndex < neighbors.size(); edgeIndex++) { // Duyệt qua từng cạnh xuất phát từ fromId
                    int toId = neighbors.get(edgeIndex); // Lấy mã thành phố đích của cạnh
                    int weight = weights.get(edgeIndex); // Lấy trọng số của cạnh đang xét
                    int newDistance = distance[fromId] + weight; // Tính khoảng cách mới qua cạnh này
                    if (newDistance < distance[toId]) { // Nếu khoảng cách mới tốt hơn
                        distance[toId] = newDistance; // Cập nhật khoảng cách tốt hơn cho thành phố đích
                        updated = true; // Đánh dấu đã có cập nhật trong vòng lặp
                    }
                }
            }
            if (!updated) { // Nếu không có cập nhật nào trong vòng lặp
                break; // Có thể dừng sớm vì khoảng cách đã tối ưu
            }
        }
        for (int fromId = 0; fromId < n; fromId++) { // Kiểm tra sự tồn tại của chu trình âm
            if (distance[fromId] == INF) { // Nếu không thể đi tới thành phố này thì bỏ qua
                continue; // Tiếp tục sang thành phố khác
            }
            List<Integer> neighbors = adjacencyList.get(fromId); // Lấy danh sách hàng xóm của fromId
            List<Integer> weights = weightList.get(fromId); // Lấy trọng số tương ứng
            for (int edgeIndex = 0; edgeIndex < neighbors.size(); edgeIndex++) { // Duyệt qua các cạnh
                int toId = neighbors.get(edgeIndex); // Lấy mã thành phố đích
                int weight = weights.get(edgeIndex); // Lấy trọng số cạnh
                if (distance[fromId] + weight < distance[toId]) { // Nếu vẫn có thể giảm khoảng cách
                    throw new IllegalStateException("Do thi co chu trinh am"); // Ném ngoại lệ báo hiệu tồn tại chu trình âm
                }
            }
        }
        return distance; // Trả về mảng khoảng cách cuối cùng từ nguồn tới mọi thành phố
    }

    public int getInfinityValue() { // Trả về giá trị vô cực để tiện in kết quả bên ngoài
        return INF; // Hoàn trả hằng số INF đã khai báo trong lớp
    }

    public List<City> getCities() { // Trả về danh sách toàn bộ thành phố trong đồ thị
        return new ArrayList<>(cities); // Tạo bản sao để tránh bị sửa từ bên ngoài
    }
}
