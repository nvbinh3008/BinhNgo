// The BoatBookingSystem.java file contains all classes for the Boat Booking System application.
import java.io.BufferedReader; // Importing BufferedReader for reading text from a file.
import java.io.BufferedWriter; // Importing BufferedWriter for writing text to a file.
import java.io.FileReader; // Importing FileReader to read from files.
import java.io.FileWriter; // Importing FileWriter to write to files.
import java.io.IOException; // Importing IOException to handle input/output exceptions.
import java.util.ArrayDeque; // Importing ArrayDeque to implement the queue used in BFS traversal.
import java.util.Deque; // Importing Deque interface for queue operations.
import java.util.InputMismatchException; // Importing InputMismatchException to handle invalid input types.
import java.util.Scanner; // Importing Scanner for interactive console input.

// The Boat class stores information about a boat entity.
class Boat {
    // Field storing the boat code that uniquely identifies the boat.
    String bcode; // Boat code field.
    // Field storing the boat name.
    String boatName; // Boat name field.
    // Field storing the total number of seats on the boat.
    int seat; // Total seat count field.
    // Field storing the number of booked seats.
    int booked; // Booked seat count field.
    // Field storing the departure place of the boat.
    String departPlace; // Departure place field.
    // Field storing the rate of the boat.
    double rate; // Rate field.

    // Constructor used to create a boat with all necessary attributes.
    Boat(String bcode, String boatName, int seat, int booked, String departPlace, double rate) {
        this.bcode = bcode; // Assigning boat code to the object.
        this.boatName = boatName; // Assigning boat name to the object.
        this.seat = seat; // Assigning total seats to the object.
        this.booked = booked; // Assigning booked seats to the object.
        this.departPlace = departPlace; // Assigning departure place to the object.
        this.rate = rate; // Assigning rate to the object.
    }

    // Method converting the boat to a formatted string representation.
    /* Step 1: Start building a StringBuilder to store the textual representation.
       Step 2: Append each field separated by the delimiter.
       Step 3: Convert the builder to a string and return it. */
    public String toDataString() {
        StringBuilder builder = new StringBuilder(); // Creating a new StringBuilder instance.
        builder.append(bcode); // Appending the boat code to the builder.
        builder.append("|"); // Appending a delimiter.
        builder.append(boatName); // Appending the boat name to the builder.
        builder.append("|"); // Appending a delimiter.
        builder.append(seat); // Appending the seat number to the builder.
        builder.append("|"); // Appending a delimiter.
        builder.append(booked); // Appending the booked seats to the builder.
        builder.append("|"); // Appending a delimiter.
        builder.append(departPlace); // Appending the departure place.
        builder.append("|"); // Appending a delimiter.
        builder.append(rate); // Appending the rate value.
        return builder.toString(); // Returning the constructed string.
    }

    // Method creating a human-readable description of the boat.
    /* Step 1: Start building a StringBuilder for the description.
       Step 2: Append each field with labels for clarity.
       Step 3: Return the formatted string. */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(); // Creating a new StringBuilder instance.
        builder.append(String.format("%-10s", bcode)); // Appending the boat code formatted with padding.
        builder.append(String.format("%-20s", boatName)); // Appending the boat name with padding.
        builder.append(String.format("%-8d", seat)); // Appending the seat count formatted.
        builder.append(String.format("%-8d", booked)); // Appending the booked count formatted.
        builder.append(String.format("%-20s", departPlace)); // Appending the departure place formatted.
        builder.append(String.format("%-10.2f", rate)); // Appending the rate formatted with two decimals.
        return builder.toString(); // Returning the composed string.
    }
}

// The BoatNode class represents a node in the binary search tree storing Boat objects.
class BoatNode {
    // Field storing the boat information.
    Boat info; // Boat object stored in the node.
    // Field storing the reference to the left child.
    BoatNode left; // Left child reference.
    // Field storing the reference to the right child.
    BoatNode right; // Right child reference.

    // Constructor creating a node with the given boat data.
    BoatNode(Boat info) {
        this.info = info; // Assigning the boat information to the node.
        this.left = null; // Initializing the left child reference to null.
        this.right = null; // Initializing the right child reference to null.
    }
}

// The BoatBST class implements the binary search tree storing boats keyed by bcode.
class BoatBST {
    // Field storing the root of the binary search tree.
    BoatNode root; // Root node of the tree.

    // Constructor initializing an empty tree.
    BoatBST() {
        this.root = null; // Setting root to null to represent an empty tree.
    }

    // Method to insert a new boat into the tree.
    /* Step 1: Create a new Boat object from input parameters.
       Step 2: If the tree is empty, assign the new node as the root.
       Step 3: Otherwise, traverse the tree using bcode comparisons.
       Step 4: If a duplicate bcode is found, do not insert and return false.
       Step 5: Insert the new node at the correct leaf position and return true. */
    public boolean insert(String bcode, String boatName, int seat, int booked, String departPlace, double rate) {
        Boat newBoat = new Boat(bcode, boatName, seat, booked, departPlace, rate); // Creating a new boat object with provided information.
        BoatNode newNode = new BoatNode(newBoat); // Wrapping the boat inside a tree node.
        if (root == null) { // Checking if the tree is currently empty.
            root = newNode; // Assigning the new node as the root of the tree.
            return true; // Returning true to indicate successful insertion.
        }
        BoatNode parent = null; // Variable storing the parent during traversal.
        BoatNode current = root; // Starting traversal from the root node.
        while (current != null) { // Continuing until a null child is found.
            parent = current; // Updating parent to the current node.
            int cmp = bcode.compareTo(current.info.bcode); // Comparing the new bcode with the current node's bcode.
            if (cmp == 0) { // Checking for duplicate bcode.
                return false; // Returning false because duplicates are not allowed.
            } else if (cmp < 0) { // Checking if the new bcode is smaller than current bcode.
                current = current.left; // Moving traversal to the left child.
            } else { // Handling the case where the new bcode is greater than current bcode.
                current = current.right; // Moving traversal to the right child.
            }
        }
        if (bcode.compareTo(parent.info.bcode) < 0) { // Checking if new bcode is smaller than parent's bcode.
            parent.left = newNode; // Setting the new node as the left child.
        } else { // Handling the case where the new bcode is greater than parent bcode.
            parent.right = newNode; // Setting the new node as the right child.
        }
        return true; // Returning true to indicate successful insertion.
    }

    // Method to perform an in-order traversal and print boat information to console.
    /* Step 1: Start traversal from the root node.
       Step 2: Recursively visit left subtree, current node, then right subtree.
       Step 3: Print each visited node's data to the console. */
    public void inOrderTraversal() {
        inOrderTraversal(root); // Calling the helper method with the root node.
    }

    // Helper method executing recursive in-order traversal.
    /* Step 1: If the current node is null, return to stop recursion.
       Step 2: Recursively traverse the left subtree.
       Step 3: Process the current node by printing it.
       Step 4: Recursively traverse the right subtree. */
    private void inOrderTraversal(BoatNode node) {
        if (node == null) { // Checking if the current node is null.
            return; // Exiting the recursion when reaching a leaf.
        }
        inOrderTraversal(node.left); // Recursively traversing the left subtree.
        System.out.println(node.info); // Printing the current node's information.
        inOrderTraversal(node.right); // Recursively traversing the right subtree.
    }

    // Method performing breadth-first traversal and printing data.
    /* Step 1: If the tree is empty, stop the traversal.
       Step 2: Initialize a queue and add the root node.
       Step 3: While the queue is not empty, remove a node.
       Step 4: Print the node and enqueue its children if they exist. */
    public void breadthFirstTraversal() {
        if (root == null) { // Checking if the tree is empty.
            return; // Ending traversal as there are no nodes to process.
        }
        Deque<BoatNode> queue = new ArrayDeque<>(); // Creating a queue to store nodes for BFS.
        queue.add(root); // Adding the root node to the queue.
        while (!queue.isEmpty()) { // Continuing until all nodes are processed.
            BoatNode node = queue.poll(); // Retrieving and removing the front node from the queue.
            System.out.println(node.info); // Printing the boat information stored in the node.
            if (node.left != null) { // Checking if the left child exists.
                queue.add(node.left); // Adding the left child to the queue.
            }
            if (node.right != null) { // Checking if the right child exists.
                queue.add(node.right); // Adding the right child to the queue.
            }
        }
    }

    // Method to write an in-order traversal to a file.
    /* Step 1: Open a BufferedWriter for the target file.
       Step 2: Recursively perform in-order traversal and append each node to the file.
       Step 3: Close the writer to persist the data. */
    public void inOrderToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) { // Opening the writer in a try-with-resources block.
            inOrderToFile(root, writer); // Invoking the helper method to write nodes.
        }
    }

    // Helper method performing recursive in-order traversal and writing to a file.
    /* Step 1: If the current node is null, return.
       Step 2: Recursively process the left subtree.
       Step 3: Write the current node data to the file followed by a newline.
       Step 4: Recursively process the right subtree. */
    private void inOrderToFile(BoatNode node, BufferedWriter writer) throws IOException {
        if (node == null) { // Checking if the node is null.
            return; // Ending recursion for null nodes.
        }
        inOrderToFile(node.left, writer); // Writing left subtree to the file.
        writer.write(node.info.toDataString()); // Writing the current node's data string to the file.
        writer.newLine(); // Inserting a new line after the current record.
        inOrderToFile(node.right, writer); // Writing right subtree to the file.
    }

    // Method to search for a boat by bcode.
    /* Step 1: Start traversal at the root node.
       Step 2: Compare the search code with the current node.
       Step 3: Move left or right based on the comparison.
       Step 4: Return the node when found or null if absent. */
    public BoatNode search(String bcode) {
        BoatNode current = root; // Starting from the root node.
        while (current != null) { // Continuing until the node is found or traversal ends.
            int cmp = bcode.compareTo(current.info.bcode); // Comparing search code with current node's code.
            if (cmp == 0) { // Checking if the codes are equal.
                return current; // Returning the current node because the boat is found.
            } else if (cmp < 0) { // Checking if the search code is smaller.
                current = current.left; // Moving to the left child to continue search.
            } else { // Handling the case where the search code is greater.
                current = current.right; // Moving to the right child to continue search.
            }
        }
        return null; // Returning null to indicate the boat was not found.
    }

    // Method deleting a boat by bcode using the copy-and-delete technique.
    /* Step 1: Locate the node and its parent.
       Step 2: If the node has two children, find the inorder predecessor, copy data, and remove predecessor.
       Step 3: If the node has one child, replace it with its child.
        Step 4: If the node is a leaf, remove it directly.
       Step 5: Update parent links accordingly. */
    public boolean deleteByCopying(String bcode) {
        BoatNode parent = null; // Variable to keep track of the parent node.
        BoatNode current = root; // Starting search from the root node.
        while (current != null && !current.info.bcode.equals(bcode)) { // Looping until the target node is found.
            parent = current; // Setting parent to current node before moving down.
            if (bcode.compareTo(current.info.bcode) < 0) { // Checking if the target code is smaller than current.
                current = current.left; // Moving to the left child.
            } else { // Handling the case where the target code is greater.
                current = current.right; // Moving to the right child.
            }
        }
        if (current == null) { // Checking if the target node was found.
            return false; // Returning false because no node with the code exists.
        }
        if (current.left != null && current.right != null) { // Checking if node has two children.
            BoatNode predecessorParent = current; // Storing the parent of predecessor.
            BoatNode predecessor = current.left; // Starting search for predecessor in left subtree.
            while (predecessor.right != null) { // Looking for the rightmost node in the left subtree.
                predecessorParent = predecessor; // Updating parent to current predecessor node.
                predecessor = predecessor.right; // Moving to the right child.
            }
            current.info = predecessor.info; // Copying predecessor's data into the target node.
            if (predecessorParent == current) { // Checking if predecessor is direct left child.
                predecessorParent.left = predecessor.left; // Replacing the left child with predecessor's left subtree.
            } else { // Handling the case where predecessor is deeper in the subtree.
                predecessorParent.right = predecessor.left; // Replacing the predecessor node with its left child.
            }
            return true; // Returning true to indicate successful deletion.
        }
        BoatNode child = (current.left != null) ? current.left : current.right; // Determining the child node if any.
        if (parent == null) { // Checking if the node to delete is the root.
            root = child; // Updating the root to the child node.
        } else if (parent.left == current) { // Checking if current is the left child of its parent.
            parent.left = child; // Connecting parent's left pointer to the child.
        } else { // Handling the case where current is the right child of its parent.
            parent.right = child; // Connecting parent's right pointer to the child.
        }
        return true; // Returning true to indicate successful deletion.
    }

    // Method to convert the tree into a balanced tree using an array intermediate.
    /* Step 1: Traverse the tree in-order to store nodes in a list.
       Step 2: Recursively build a balanced tree from the sorted list.
       Step 3: Assign the new root to the tree. */
    public void balance() {
        java.util.List<Boat> boats = new java.util.ArrayList<>(); // Creating a list to store boats in sorted order.
        storeInOrder(root, boats); // Filling the list with boats using in-order traversal.
        root = buildBalancedTree(boats, 0, boats.size() - 1); // Building a balanced tree and assigning the new root.
    }

    // Helper method to store boats in-order into a list.
    /* Step 1: If the node is null, return immediately.
       Step 2: Recursively process the left subtree.
       Step 3: Add the current node's boat to the list.
       Step 4: Recursively process the right subtree. */
    private void storeInOrder(BoatNode node, java.util.List<Boat> boats) {
        if (node == null) { // Checking if the node is null.
            return; // Ending recursion for null nodes.
        }
        storeInOrder(node.left, boats); // Processing the left subtree.
        boats.add(node.info); // Adding the current boat to the list.
        storeInOrder(node.right, boats); // Processing the right subtree.
    }

    // Helper method to build a balanced tree from a list of boats.
    /* Step 1: If start index exceeds end index, return null to end recursion.
       Step 2: Calculate the middle index to select the root for the current subtree.
       Step 3: Create a node using the middle element.
       Step 4: Recursively build left and right subtrees from subranges.
       Step 5: Return the constructed node. */
    private BoatNode buildBalancedTree(java.util.List<Boat> boats, int start, int end) {
        if (start > end) { // Checking if the subarray is empty.
            return null; // Returning null for empty ranges.
        }
        int mid = (start + end) / 2; // Calculating the middle index.
        BoatNode node = new BoatNode(boats.get(mid)); // Creating a node with the middle boat.
        node.left = buildBalancedTree(boats, start, mid - 1); // Recursively building the left subtree.
        node.right = buildBalancedTree(boats, mid + 1, end); // Recursively building the right subtree.
        return node; // Returning the constructed node.
    }

    // Method counting the number of boats stored in the tree.
    /* Step 1: Start from the root and recursively count nodes.
       Step 2: For each node, count one plus counts of its subtrees.
       Step 3: Return the total count. */
    public int countBoats() {
        return countBoats(root); // Calling the helper method starting from the root.
    }

    // Helper method to recursively count nodes in the tree.
    /* Step 1: If the node is null, return zero.
       Step 2: Recursively count the left subtree.
       Step 3: Recursively count the right subtree.
       Step 4: Sum the counts and add one for the current node. */
    private int countBoats(BoatNode node) {
        if (node == null) { // Checking if the node is null.
            return 0; // Returning zero for null nodes.
        }
        return 1 + countBoats(node.left) + countBoats(node.right); // Summing counts from left and right plus current node.
    }

    // Method loading boat data from a file and inserting into the tree.
    /* Step 1: Open the file using BufferedReader.
       Step 2: Read each line, parse boat attributes, and insert into the tree.
       Step 3: Ignore invalid lines and continue processing remaining lines.
       Step 4: Close the reader after finishing. */
    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { // Opening the file for reading using try-with-resources.
            String line; // Variable to hold each line from the file.
            while ((line = reader.readLine()) != null) { // Reading the file line by line.
                String[] parts = line.split("\\|"); // Splitting the line into parts using the delimiter.
                if (parts.length != 6) { // Checking if the line has the correct number of parts.
                    continue; // Skipping malformed lines.
                }
                String bcode = parts[0].trim(); // Extracting and trimming the boat code.
                String name = parts[1].trim(); // Extracting and trimming the boat name.
                int seat = Integer.parseInt(parts[2].trim()); // Parsing the seat count as integer.
                int booked = Integer.parseInt(parts[3].trim()); // Parsing the booked count as integer.
                String depart = parts[4].trim(); // Extracting and trimming the departure place.
                double rate = Double.parseDouble(parts[5].trim()); // Parsing the rate as double.
                insert(bcode, name, seat, booked, depart, rate); // Inserting the parsed boat into the tree.
            }
        }
    }
}

// The Customer class stores information about customers using the system.
class Customer {
    // Field storing the unique customer code.
    String ccode; // Customer code field.
    // Field storing the name of the customer.
    String cusName; // Customer name field.
    // Field storing the phone number.
    String phone; // Phone number field.

    // Constructor to initialize a new customer object.
    Customer(String ccode, String cusName, String phone) {
        this.ccode = ccode; // Assigning the customer code to the object.
        this.cusName = cusName; // Assigning the customer name to the object.
        this.phone = phone; // Assigning the phone number to the object.
    }

    // Method converting the customer to a data string for file storage.
    /* Step 1: Start a StringBuilder instance.
       Step 2: Append each field separated by a delimiter.
       Step 3: Return the built string. */
    public String toDataString() {
        StringBuilder builder = new StringBuilder(); // Creating a new StringBuilder instance.
        builder.append(ccode); // Appending the customer code to the builder.
        builder.append("|"); // Appending a delimiter.
        builder.append(cusName); // Appending the customer name to the builder.
        builder.append("|"); // Appending a delimiter.
        builder.append(phone); // Appending the phone number to the builder.
        return builder.toString(); // Returning the built string.
    }

    // Method creating a human-readable description of the customer.
    /* Step 1: Create a StringBuilder for the formatted output.
       Step 2: Append each field with alignment formatting.
       Step 3: Return the final string. */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(); // Creating a new StringBuilder instance.
        builder.append(String.format("%-10s", ccode)); // Appending the customer code with padding.
        builder.append(String.format("%-20s", cusName)); // Appending the customer name with padding.
        builder.append(String.format("%-15s", phone)); // Appending the phone number with padding.
        return builder.toString(); // Returning the composed string.
    }
}

// The CustomerNode class represents a node in the singly linked list for customers.
class CustomerNode {
    // Field storing the customer information.
    Customer info; // Customer object stored in this node.
    // Field storing the reference to the next node in the list.
    CustomerNode next; // Reference to the next node.

    // Constructor creating a node with customer data.
    CustomerNode(Customer info) {
        this.info = info; // Assigning the customer information to the node.
        this.next = null; // Initializing the next reference to null.
    }
}

// The CustomerList class manages a singly linked list of customers.
class CustomerList {
    // Field storing the head of the list.
    CustomerNode head; // Head node of the list.
    // Field storing the tail of the list for efficient append operations.
    CustomerNode tail; // Tail node of the list.

    // Constructor initializing an empty list.
    CustomerList() {
        this.head = null; // Setting head to null for an empty list.
        this.tail = null; // Setting tail to null for an empty list.
    }

    // Method to append a new customer to the end of the list.
    /* Step 1: Create a new node for the customer.
       Step 2: If the list is empty, set head and tail to the new node.
       Step 3: Otherwise, attach the new node to the tail and update tail.
       Step 4: Return true when insertion succeeds. */
    public boolean addLast(String ccode, String cusName, String phone) {
        Customer newCustomer = new Customer(ccode, cusName, phone); // Creating a new customer object with provided data.
        if (findByCode(ccode) != null) { // Checking if a customer with the same code already exists.
            return false; // Returning false to avoid duplicate entries.
        }
        CustomerNode node = new CustomerNode(newCustomer); // Creating a new node to store the customer.
        if (head == null) { // Checking if the list is currently empty.
            head = node; // Setting the head to the new node.
            tail = node; // Setting the tail to the new node.
        } else { // Handling the case where the list already has elements.
            tail.next = node; // Attaching the new node after the current tail.
            tail = node; // Updating the tail reference to the new node.
        }
        return true; // Returning true to indicate successful addition.
    }

    // Method to display all customers in the list.
    /* Step 1: Start from the head node.
       Step 2: Traverse the list node by node.
       Step 3: Print each customer's information. */
    public void display() {
        CustomerNode current = head; // Starting traversal from the head.
        while (current != null) { // Continuing until reaching the end of the list.
            System.out.println(current.info); // Printing the current customer's information.
            current = current.next; // Moving to the next node in the list.
        }
    }

    // Method to find a customer by code.
    /* Step 1: Start traversal at the head.
       Step 2: Compare each node's code with the target.
       Step 3: Return the node when a match is found or null if absent. */
    public CustomerNode findByCode(String ccode) {
        CustomerNode current = head; // Starting traversal from the head node.
        while (current != null) { // Continuing until the end of the list.
            if (current.info.ccode.equals(ccode)) { // Checking if current node's code matches the target.
                return current; // Returning the node when a match is found.
            }
            current = current.next; // Moving to the next node when not matched.
        }
        return null; // Returning null if no matching customer is found.
    }

    // Method to delete a customer by code.
    /* Step 1: Handle empty list by returning false.
       Step 2: If the head matches the code, remove it and adjust pointers.
       Step 3: Otherwise, traverse to find the previous node of the target.
       Step 4: Update links to exclude the target node and adjust tail if necessary. */
    public boolean deleteByCode(String ccode) {
        if (head == null) { // Checking if the list is empty.
            return false; // Returning false because there is nothing to delete.
        }
        if (head.info.ccode.equals(ccode)) { // Checking if the head node matches the code.
            head = head.next; // Moving head to the next node to delete the first node.
            if (head == null) { // Checking if the list became empty after deletion.
                tail = null; // Setting tail to null when the list is empty.
            }
            return true; // Returning true to indicate successful deletion.
        }
        CustomerNode current = head; // Starting traversal from the head node.
        while (current.next != null && !current.next.info.ccode.equals(ccode)) { // Looking ahead to find the node before the target.
            current = current.next; // Moving to the next node.
        }
        if (current.next == null) { // Checking if the target node was not found.
            return false; // Returning false because deletion cannot happen.
        }
        if (current.next == tail) { // Checking if the node to delete is the tail.
            tail = current; // Updating the tail reference to the current node.
        }
        current.next = current.next.next; // Bypassing the target node to remove it from the list.
        return true; // Returning true to indicate successful deletion.
    }

    // Method to load customers from a file and append to the list.
    /* Step 1: Open the file using BufferedReader.
       Step 2: Read each line, parse customer attributes, and add to the list.
       Step 3: Skip invalid lines.
       Step 4: Close the reader automatically. */
    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { // Opening the file in a try-with-resources block.
            String line; // Variable to hold each line.
            while ((line = reader.readLine()) != null) { // Reading lines until reaching the end of the file.
                String[] parts = line.split("\\|"); // Splitting the line using the delimiter.
                if (parts.length != 3) { // Checking if the line has exactly three parts.
                    continue; // Skipping malformed lines.
                }
                String ccode = parts[0].trim(); // Extracting the customer code.
                String name = parts[1].trim(); // Extracting the customer name.
                String phone = parts[2].trim(); // Extracting the phone number.
                addLast(ccode, name, phone); // Adding the customer to the list.
            }
        }
    }

    // Method to save the customer list to a file.
    /* Step 1: Open the target file using BufferedWriter.
       Step 2: Traverse the list and write each customer as a data string.
       Step 3: Close the writer automatically. */
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) { // Opening the writer in a try-with-resources block.
            CustomerNode current = head; // Starting traversal from the head.
            while (current != null) { // Continuing until the end of the list.
                writer.write(current.info.toDataString()); // Writing the current customer's data string.
                writer.newLine(); // Adding a newline after each record.
                current = current.next; // Moving to the next node in the list.
            }
        }
    }
}

// The Booking class stores information about a booking transaction.
class Booking {
    // Field storing the boat code for the booking.
    String bcode; // Boat code field.
    // Field storing the customer code associated with the booking.
    String ccode; // Customer code field.
    // Field storing the number of seats booked.
    int seat; // Seats booked field.

    // Constructor to initialize booking data.
    Booking(String bcode, String ccode, int seat) {
        this.bcode = bcode; // Assigning the boat code to the booking.
        this.ccode = ccode; // Assigning the customer code to the booking.
        this.seat = seat; // Assigning the number of seats to the booking.
    }

    // Method converting the booking to a data string for files.
    /* Step 1: Start a StringBuilder instance.
       Step 2: Append each field separated by a delimiter.
       Step 3: Return the resulting string. */
    public String toDataString() {
        StringBuilder builder = new StringBuilder(); // Creating a new StringBuilder instance.
        builder.append(bcode); // Appending the boat code.
        builder.append("|"); // Appending a delimiter.
        builder.append(ccode); // Appending the customer code.
        builder.append("|"); // Appending a delimiter.
        builder.append(seat); // Appending the seat count.
        return builder.toString(); // Returning the string representation.
    }

    // Method converting the booking to a readable string.
    /* Step 1: Create a StringBuilder for formatting.
       Step 2: Append each field with alignment formatting.
       Step 3: Return the formatted string. */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(); // Creating a new StringBuilder instance.
        builder.append(String.format("%-10s", bcode)); // Appending the boat code with padding.
        builder.append(String.format("%-10s", ccode)); // Appending the customer code with padding.
        builder.append(String.format("%-8d", seat)); // Appending the seat count with padding.
        return builder.toString(); // Returning the formatted string.
    }
}

// The BookingNode class represents a node in the booking linked list.
class BookingNode {
    // Field storing the booking information.
    Booking info; // Booking object stored in the node.
    // Field storing the reference to the next node.
    BookingNode next; // Reference to the next node.

    // Constructor creating a node with booking data.
    BookingNode(Booking info) {
        this.info = info; // Assigning the booking to the node.
        this.next = null; // Initializing the next reference to null.
    }
}

// The BookingList class manages booking records in a singly linked list.
class BookingList {
    // Field storing the head of the booking list.
    BookingNode head; // Head node of the list.
    // Field storing the tail of the booking list for efficient append operations.
    BookingNode tail; // Tail node of the list.

    // Constructor initializing an empty booking list.
    BookingList() {
        this.head = null; // Setting head to null for an empty list.
        this.tail = null; // Setting tail to null for an empty list.
    }

    // Method to append a new booking after validating constraints.
    /* Step 1: Validate that the boat exists in the tree.
       Step 2: Validate that the customer exists in the list.
       Step 3: Check available seats on the boat.
       Step 4: Deduct seats and append the booking to the list.
       Step 5: Return true if booking succeeds. */
    public boolean addBooking(BoatBST boatTree, CustomerList customerList, String bcode, String ccode, int seat) {
        BoatNode boatNode = boatTree.search(bcode); // Searching for the boat using the provided code.
        if (boatNode == null) { // Checking if the boat exists.
            return false; // Returning false when the boat is absent.
        }
        CustomerNode customerNode = customerList.findByCode(ccode); // Searching for the customer.
        if (customerNode == null) { // Checking if the customer exists.
            return false; // Returning false when the customer is absent.
        }
        if (seat <= 0) { // Validating that requested seats are positive.
            return false; // Returning false for invalid seat count.
        }
        int available = boatNode.info.seat - boatNode.info.booked; // Calculating the remaining available seats.
        if (seat > available) { // Checking if there are enough seats.
            return false; // Returning false if not enough seats remain.
        }
        boatNode.info.booked += seat; // Increasing the booked seats on the boat.
        Booking booking = new Booking(bcode, ccode, seat); // Creating a new booking object.
        BookingNode node = new BookingNode(booking); // Wrapping the booking in a list node.
        if (head == null) { // Checking if the booking list is empty.
            head = node; // Setting head to the new node.
            tail = node; // Setting tail to the new node.
        } else { // Handling the case where the list already has elements.
            tail.next = node; // Attaching the new node after the current tail.
            tail = node; // Updating the tail reference to the new node.
        }
        return true; // Returning true to indicate successful booking.
    }

    // Method to display all bookings in the list.
    /* Step 1: Start from the head node of the booking list.
       Step 2: Traverse each node sequentially.
       Step 3: Print booking information for each node. */
    public void display() {
        BookingNode current = head; // Starting traversal from the head.
        while (current != null) { // Continuing until reaching the end of the list.
            System.out.println(current.info); // Printing the current booking information.
            current = current.next; // Moving to the next node in the list.
        }
    }

    // Method to sort bookings by boat code then customer code using insertion sort on the list.
    /* Step 1: Create a new sorted list starting as empty.
       Step 2: Traverse the current list and insert each node into the sorted list in order.
       Step 3: Update head and tail references to the sorted list. */
    public void sort() {
        BookingNode sortedHead = null; // Initializing the head of the sorted list as null.
        BookingNode current = head; // Starting traversal of the original list.
        while (current != null) { // Continuing until all nodes are processed.
            BookingNode next = current.next; // Storing the next node before re-linking.
            sortedHead = insertSorted(sortedHead, current); // Inserting the current node into the sorted list.
            current = next; // Moving to the next node to be inserted.
        }
        head = sortedHead; // Updating the head to the sorted list head.
        tail = head; // Resetting tail to head before traversal.
        if (tail != null) { // Checking if the list is not empty.
            while (tail.next != null) { // Traversing to the end to find the new tail.
                tail = tail.next; // Moving tail reference to the next node.
            }
        }
    }

    // Helper method to insert a node into the sorted list.
    /* Step 1: If the sorted list is empty, set node as the head.
       Step 2: If node should be placed before the current head, insert at beginning.
       Step 3: Otherwise, traverse until finding the insertion point.
       Step 4: Insert the node and return the sorted head. */
    private BookingNode insertSorted(BookingNode sortedHead, BookingNode node) {
        node.next = null; // Ensuring the node's next reference is cleared before insertion.
        if (sortedHead == null) { // Checking if the sorted list is empty.
            return node; // Returning node as the new head.
        }
        if (compare(node.info, sortedHead.info) < 0) { // Checking if node should be placed before head.
            node.next = sortedHead; // Linking node before the current head.
            return node; // Returning node as the new head.
        }
        BookingNode current = sortedHead; // Starting traversal from the sorted head.
        while (current.next != null && compare(node.info, current.next.info) >= 0) { // Moving until proper position is found.
            current = current.next; // Advancing to the next node in sorted list.
        }
        node.next = current.next; // Linking node after current.
        current.next = node; // Updating current's next to the new node.
        return sortedHead; // Returning the head of the sorted list.
    }

    // Helper method comparing two booking records by boat code then customer code.
    /* Step 1: Compare boat codes of the two bookings.
       Step 2: If boat codes differ, return the comparison result.
       Step 3: Otherwise compare customer codes and return the result. */
    private int compare(Booking a, Booking b) {
        int cmp = a.bcode.compareTo(b.bcode); // Comparing boat codes of the bookings.
        if (cmp != 0) { // Checking if boat codes are different.
            return cmp; // Returning the comparison result when boat codes differ.
        }
        return a.ccode.compareTo(b.ccode); // Comparing customer codes when boat codes are equal.
    }
}

// The BoatBookingSystem class provides the main entry point and menu-driven interface.
public class BoatBookingSystem {
    // Field storing the boat binary search tree instance.
    private BoatBST boatTree; // Tree containing boat data.
    // Field storing the customer linked list instance.
    private CustomerList customerList; // Linked list containing customers.
    // Field storing the booking linked list instance.
    private BookingList bookingList; // Linked list containing bookings.
    // Field storing the scanner for reading user input.
    private Scanner scanner; // Scanner for console input.

    // Constructor initializing all data structures and scanner.
    BoatBookingSystem() {
        boatTree = new BoatBST(); // Creating a new boat binary search tree.
        customerList = new CustomerList(); // Creating a new customer linked list.
        bookingList = new BookingList(); // Creating a new booking linked list.
        scanner = new Scanner(System.in); // Creating a new Scanner for reading user input.
    }

    // Method starting the menu loop to interact with the user.
    /* Step 1: Display the main menu options.
       Step 2: Read the user's choice from input.
       Step 3: Execute the corresponding action based on the choice.
       Step 4: Repeat until the user selects exit. */
    public void run() {
        boolean running = true; // Flag controlling the menu loop.
        while (running) { // Looping until the user decides to exit.
            printMenu(); // Displaying the available menu options.
            System.out.print("Choose an option: "); // Prompting the user for input.
            String choice = scanner.nextLine().trim(); // Reading the user's choice and trimming whitespace.
            switch (choice) { // Evaluating the user's choice.
                case "1.1": // Handling boat data loading from file.
                    handleLoadBoat(); // Invoking method to load boat data.
                    break; // Breaking after handling the choice.
                case "1.2": // Handling boat insertion.
                    handleInsertBoat(); // Invoking method to insert a new boat.
                    break; // Breaking after handling the choice.
                case "1.3": // Handling in-order traversal display.
                    boatTree.inOrderTraversal(); // Calling traversal to print boats in order.
                    break; // Breaking after handling the choice.
                case "1.4": // Handling breadth-first traversal display.
                    boatTree.breadthFirstTraversal(); // Calling BFS traversal to print boats level by level.
                    break; // Breaking after handling the choice.
                case "1.5": // Handling in-order traversal to file.
                    handleInOrderToFile(); // Invoking method to write traversal to a file.
                    break; // Breaking after handling the choice.
                case "1.6": // Handling search by boat code.
                    handleSearchBoat(); // Invoking method to search for a boat.
                    break; // Breaking after handling the choice.
                case "1.7": // Handling deletion by boat code.
                    handleDeleteBoat(); // Invoking method to delete a boat using copying.
                    break; // Breaking after handling the choice.
                case "1.8": // Handling tree balancing.
                    boatTree.balance(); // Calling balance method to rebuild the tree.
                    System.out.println("Boat tree balanced."); // Informing the user that balancing is complete.
                    break; // Breaking after handling the choice.
                case "1.9": // Handling counting boats.
                    System.out.println("Number of boats: " + boatTree.countBoats()); // Printing the total number of boats.
                    break; // Breaking after handling the choice.
                case "2.1": // Handling customer data loading from file.
                    handleLoadCustomer(); // Invoking method to load customers from file.
                    break; // Breaking after handling the choice.
                case "2.2": // Handling adding a customer.
                    handleAddCustomer(); // Invoking method to add a new customer to the list.
                    break; // Breaking after handling the choice.
                case "2.3": // Handling display of customers.
                    customerList.display(); // Displaying all customers in the list.
                    break; // Breaking after handling the choice.
                case "2.4": // Handling saving customers to file.
                    handleSaveCustomers(); // Invoking method to save customer list to a file.
                    break; // Breaking after handling the choice.
                case "2.5": // Handling search for customer by code.
                    handleSearchCustomer(); // Invoking method to search for a customer.
                    break; // Breaking after handling the choice.
                case "2.6": // Handling deletion of a customer by code.
                    handleDeleteCustomer(); // Invoking method to delete a customer.
                    break; // Breaking after handling the choice.
                case "3.1": // Handling booking input.
                    handleAddBooking(); // Invoking method to add a new booking.
                    break; // Breaking after handling the choice.
                case "3.2": // Handling display of bookings.
                    bookingList.display(); // Displaying all booking records.
                    break; // Breaking after handling the choice.
                case "3.3": // Handling sorting of bookings.
                    bookingList.sort(); // Sorting the booking list by boat code and customer code.
                    System.out.println("Bookings sorted by boat code and customer code."); // Informing the user about sorting completion.
                    break; // Breaking after handling the choice.
                case "0": // Handling exit option.
                    running = false; // Setting flag to false to exit the loop.
                    System.out.println("Exiting Boat Booking System."); // Informing the user about exit.
                    break; // Breaking after handling the choice.
                default: // Handling invalid options.
                    System.out.println("Invalid option. Please try again."); // Informing the user about invalid input.
                    break; // Breaking after handling the choice.
            }
        }
    }

    // Method displaying the menu options.
    /* Step 1: Print the headers and categories for clarity.
       Step 2: Print each numbered option on the console. */
    private void printMenu() {
        System.out.println("\nBoat Booking System Menu"); // Printing the menu title with a blank line before it.
        System.out.println("Products (Boats):"); // Printing the products section header.
        System.out.println("1.1. Load data from file"); // Printing option 1.1.
        System.out.println("1.2. Input & insert data"); // Printing option 1.2.
        System.out.println("1.3. In-order traverse"); // Printing option 1.3.
        System.out.println("1.4. Breadth-first traverse"); // Printing option 1.4.
        System.out.println("1.5. In-order traverse to file"); // Printing option 1.5.
        System.out.println("1.6. Search by bcode"); // Printing option 1.6.
        System.out.println("1.7. Delete by bcode by copying"); // Printing option 1.7.
        System.out.println("1.8. Simply balancing"); // Printing option 1.8.
        System.out.println("1.9. Count number of boats"); // Printing option 1.9.
        System.out.println("Customers:"); // Printing the customers section header.
        System.out.println("2.1. Load data from file"); // Printing option 2.1.
        System.out.println("2.2. Input & add to the end"); // Printing option 2.2.
        System.out.println("2.3. Display data"); // Printing option 2.3.
        System.out.println("2.4. Save customer list to file"); // Printing option 2.4.
        System.out.println("2.5. Search by ccode"); // Printing option 2.5.
        System.out.println("2.6. Delete by ccode"); // Printing option 2.6.
        System.out.println("Bookings:"); // Printing the bookings section header.
        System.out.println("3.1. Input data"); // Printing option 3.1.
        System.out.println("3.2. Display booking data"); // Printing option 3.2.
        System.out.println("3.3. Sort by bcode + ccode"); // Printing option 3.3.
        System.out.println("0. Exit"); // Printing the exit option.
    }

    // Method handling the loading of boat data from a file.
    /* Step 1: Prompt the user to enter the file name.
       Step 2: Attempt to load boats from the file via the tree.
       Step 3: Inform the user about success or failure. */
    private void handleLoadBoat() {
        System.out.print("Enter boat data file name: "); // Prompting the user for the file name.
        String filename = scanner.nextLine().trim(); // Reading the file name from input.
        try { // Starting a try block to handle IOExceptions.
            boatTree.loadFromFile(filename); // Loading boats from the specified file.
            System.out.println("Boat data loaded successfully."); // Informing the user about success.
        } catch (IOException | NumberFormatException e) { // Catching IOExceptions and number format errors.
            System.out.println("Failed to load boat data: " + e.getMessage()); // Informing the user about failure with error message.
        }
    }

    // Method handling insertion of a new boat via user input.
    /* Step 1: Prompt the user for each boat attribute.
       Step 2: Validate numeric fields using parsing.
       Step 3: Call the tree insertion method.
       Step 4: Inform the user about success or duplicate code. */
    private void handleInsertBoat() {
        try { // Starting a try block to catch invalid number formats.
            System.out.print("Enter boat code: "); // Prompting for boat code.
            String bcode = scanner.nextLine().trim(); // Reading boat code.
            System.out.print("Enter boat name: "); // Prompting for boat name.
            String name = scanner.nextLine().trim(); // Reading boat name.
            System.out.print("Enter seat count: "); // Prompting for seat count.
            int seat = Integer.parseInt(scanner.nextLine().trim()); // Parsing seat count.
            System.out.print("Enter booked seats: "); // Prompting for booked seats.
            int booked = Integer.parseInt(scanner.nextLine().trim()); // Parsing booked seats.
            System.out.print("Enter departure place: "); // Prompting for departure place.
            String depart = scanner.nextLine().trim(); // Reading departure place.
            System.out.print("Enter rate: "); // Prompting for rate.
            double rate = Double.parseDouble(scanner.nextLine().trim()); // Parsing rate.
            if (seat <= 0 || booked < 0 || booked > seat || rate <= 0) { // Validating numeric constraints.
                System.out.println("Invalid numeric values for seat/booked/rate."); // Informing user about invalid input.
                return; // Exiting method without insertion.
            }
            boolean inserted = boatTree.insert(bcode, name, seat, booked, depart, rate); // Attempting to insert the boat.
            if (inserted) { // Checking if insertion succeeded.
                System.out.println("Boat inserted successfully."); // Informing user about success.
            } else { // Handling duplicate code case.
                System.out.println("Boat code already exists. Insertion failed."); // Informing user about failure.
            }
        } catch (NumberFormatException e) { // Catching invalid number formats.
            System.out.println("Invalid number format: " + e.getMessage()); // Informing user about the error.
        }
    }

    // Method handling writing the in-order traversal to a file.
    /* Step 1: Prompt user for output file name.
       Step 2: Execute the tree's in-order to file method.
       Step 3: Inform user about success or failure. */
    private void handleInOrderToFile() {
        System.out.print("Enter output file name: "); // Prompting user for file name.
        String filename = scanner.nextLine().trim(); // Reading file name from input.
        try { // Starting try block to handle IOExceptions.
            boatTree.inOrderToFile(filename); // Writing tree data to file.
            System.out.println("Boat data written to file successfully."); // Informing user about success.
        } catch (IOException e) { // Catching IOExceptions from writing.
            System.out.println("Failed to write to file: " + e.getMessage()); // Informing user about failure.
        }
    }

    // Method handling search for a boat by code.
    /* Step 1: Prompt the user for the boat code.
       Step 2: Use the tree search method to find the boat.
       Step 3: Display the result or notify when not found. */
    private void handleSearchBoat() {
        System.out.print("Enter boat code to search: "); // Prompting for boat code.
        String bcode = scanner.nextLine().trim(); // Reading boat code from input.
        BoatNode node = boatTree.search(bcode); // Searching for the boat in the tree.
        if (node != null) { // Checking if the boat was found.
            System.out.println("Boat found: " + node.info); // Displaying boat information.
        } else { // Handling case where boat is absent.
            System.out.println("Boat not found."); // Informing user that boat does not exist.
        }
    }

    // Method handling deletion of a boat by code using copy technique.
    /* Step 1: Prompt user for the boat code to delete.
       Step 2: Call the deleteByCopying method on the tree.
       Step 3: Inform the user about success or failure. */
    private void handleDeleteBoat() {
        System.out.print("Enter boat code to delete: "); // Prompting for boat code.
        String bcode = scanner.nextLine().trim(); // Reading boat code from input.
        boolean deleted = boatTree.deleteByCopying(bcode); // Attempting to delete the boat.
        if (deleted) { // Checking if deletion succeeded.
            System.out.println("Boat deleted successfully."); // Informing user about success.
        } else { // Handling case where deletion failed.
            System.out.println("Boat not found or deletion failed."); // Informing user about failure.
        }
    }

    // Method handling loading customers from a file.
    /* Step 1: Prompt user for the customer file name.
       Step 2: Load customers using the linked list loader.
       Step 3: Inform user about the outcome. */
    private void handleLoadCustomer() {
        System.out.print("Enter customer data file name: "); // Prompting user for file name.
        String filename = scanner.nextLine().trim(); // Reading file name from input.
        try { // Starting try block to handle exceptions.
            customerList.loadFromFile(filename); // Loading customers from file.
            System.out.println("Customer data loaded successfully."); // Informing user about success.
        } catch (IOException e) { // Catching IOExceptions during load.
            System.out.println("Failed to load customer data: " + e.getMessage()); // Informing user about failure.
        }
    }

    // Method handling addition of a new customer via input.
    /* Step 1: Prompt for customer attributes.
       Step 2: Validate phone number to contain digits only.
       Step 3: Add the customer to the end of the list.
       Step 4: Inform user about success or duplicate code. */
    private void handleAddCustomer() {
        System.out.print("Enter customer code: "); // Prompting for customer code.
        String ccode = scanner.nextLine().trim(); // Reading customer code from input.
        System.out.print("Enter customer name: "); // Prompting for customer name.
        String name = scanner.nextLine().trim(); // Reading customer name from input.
        System.out.print("Enter phone number: "); // Prompting for phone number.
        String phone = scanner.nextLine().trim(); // Reading phone number from input.
        if (!phone.matches("\\d+")) { // Validating that phone contains digits only.
            System.out.println("Phone number must contain digits only."); // Informing user about invalid phone number.
            return; // Exiting method without adding customer.
        }
        boolean added = customerList.addLast(ccode, name, phone); // Attempting to add customer to the list.
        if (added) { // Checking if addition succeeded.
            System.out.println("Customer added successfully."); // Informing user about success.
        } else { // Handling duplicate code case.
            System.out.println("Customer code already exists."); // Informing user about failure.
        }
    }

    // Method handling saving customers to a file.
    /* Step 1: Prompt user for output file name.
       Step 2: Invoke the linked list save method.
       Step 3: Inform user about success or failure. */
    private void handleSaveCustomers() {
        System.out.print("Enter output file name: "); // Prompting for file name.
        String filename = scanner.nextLine().trim(); // Reading file name from input.
        try { // Starting try block to handle IOExceptions.
            customerList.saveToFile(filename); // Saving customer data to file.
            System.out.println("Customer data saved successfully."); // Informing user about success.
        } catch (IOException e) { // Catching IOExceptions from writing.
            System.out.println("Failed to save customer data: " + e.getMessage()); // Informing user about failure.
        }
    }

    // Method handling search for a customer by code.
    /* Step 1: Prompt user for customer code.
       Step 2: Search the list using findByCode.
       Step 3: Display the result or notify when absent. */
    private void handleSearchCustomer() {
        System.out.print("Enter customer code to search: "); // Prompting for customer code.
        String ccode = scanner.nextLine().trim(); // Reading customer code from input.
        CustomerNode node = customerList.findByCode(ccode); // Searching for the customer in the list.
        if (node != null) { // Checking if customer was found.
            System.out.println("Customer found: " + node.info); // Displaying customer information.
        } else { // Handling case where customer is absent.
            System.out.println("Customer not found."); // Informing user about absence.
        }
    }

    // Method handling deletion of a customer by code.
    /* Step 1: Prompt user for customer code to delete.
       Step 2: Invoke deleteByCode on the list.
       Step 3: Inform user about success or failure. */
    private void handleDeleteCustomer() {
        System.out.print("Enter customer code to delete: "); // Prompting for customer code.
        String ccode = scanner.nextLine().trim(); // Reading customer code from input.
        boolean deleted = customerList.deleteByCode(ccode); // Attempting to delete the customer.
        if (deleted) { // Checking if deletion succeeded.
            System.out.println("Customer deleted successfully."); // Informing user about success.
        } else { // Handling case where deletion failed.
            System.out.println("Customer not found or deletion failed."); // Informing user about failure.
        }
    }

    // Method handling addition of a new booking.
    /* Step 1: Prompt for boat code, customer code, and seat count.
       Step 2: Attempt to add the booking while validating references.
       Step 3: Inform user about success or failure. */
    private void handleAddBooking() {
        try { // Starting try block to catch number format issues.
            System.out.print("Enter boat code: "); // Prompting for boat code.
            String bcode = scanner.nextLine().trim(); // Reading boat code from input.
            System.out.print("Enter customer code: "); // Prompting for customer code.
            String ccode = scanner.nextLine().trim(); // Reading customer code from input.
            System.out.print("Enter seat count: "); // Prompting for seat count.
            int seat = Integer.parseInt(scanner.nextLine().trim()); // Parsing seat count from input.
            boolean added = bookingList.addBooking(boatTree, customerList, bcode, ccode, seat); // Attempting to add booking.
            if (added) { // Checking if booking succeeded.
                System.out.println("Booking created successfully."); // Informing user about success.
            } else { // Handling case where booking failed.
                System.out.println("Booking failed. Check codes and seat availability."); // Informing user about failure.
            }
        } catch (NumberFormatException e) { // Catching invalid number formats.
            System.out.println("Invalid number format: " + e.getMessage()); // Informing user about the error.
        }
    }

    // Main method launching the Boat Booking System.
    /* Step 1: Create a new BoatBookingSystem instance.
       Step 2: Call the run method to start the menu loop. */
    public static void main(String[] args) {
        BoatBookingSystem system = new BoatBookingSystem(); // Creating a new instance of the system.
        system.run(); // Starting the menu-driven interface.
    }
}
