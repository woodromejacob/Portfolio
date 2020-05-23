package editortrees;

import editortrees.Node.Code;

/**
 *  A height-balanced binary tree with rank that could be the basis for a text editor.
 *  
 *  @author woodrojc 
 *  DONE: Acknowledge anyone else you got help from here, along with the help they provided:
 *  I collaborated ideas with Landon Bundy.  We mostly helped each other with ideas for various method and figuring out
 *  some things with debugging.  
 *  
 *  
 */
public class EditTree {

	Node root;
	private int size;
	private int rotationCount;
	DisplayableBinaryTree display;

	/**
	 * MILESTONE 1 Construct an empty tree
	 */
	public EditTree() {
		// only things to identify an empty tree is by root being null node and size = 0, size already 0 so just make sure root is null node
		root = Node.NULL_NODE;
		
	}

	/**
	 * MILESTONE 1 Construct a single-node tree whose element is ch
	 * 
	 * @param ch
	 */
	public EditTree(char ch) {
		// now we get the root as the param, so we just set root to a node with data param. must update the size as well 
		root = new Node(ch);
		size = 1;
	}

	/**
	 * MILESTONE 2 Make this tree be a copy of e, with all new nodes, but the same
	 * shape and contents. You can write this one recursively, but you may not want
	 * your helper to be in the Node class.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) {
		if (e.root != Node.NULL_NODE) {
			root = new Node(e.root.data, e.root.rank, e.root.balance);
			size = 1;
			makeNodes(root, e.root);
		}
		else {
			size = 0;
			root = Node.NULL_NODE;
		}
	}

	/**
	 * Makes nodes for constructor with EditTree parameter
	 * @param n
	 * @param cur
	 */
	private void makeNodes(Node n, Node cur) {
		//simply assigning nodes throughout
		Node curLeft = cur.left;
		Node curRight = cur.right;
		if (curLeft != Node.NULL_NODE) {
			n.left = new Node(curLeft.data, curLeft.rank, curLeft.balance);
			size++;
			makeNodes(n.left, curLeft);
		}
		else {
			n.left = Node.NULL_NODE;
		}
		if (curRight != Node.NULL_NODE) {
			n.right = new Node(curRight.data, curRight.rank, curRight.balance);
			size++;
			makeNodes(n.right, curRight);
		}
		else {
			n.right = Node.NULL_NODE;
		}
	}

	/**
	 * MILESTONE 3 Create an EditTree whose toString is s. This can be done in O(N)
	 * time, where N is the size of the tree (note that repeatedly calling insert()
	 * would be O(N log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) {
		root = makeInOrderNodes(root, s, 0, s.length() - 1);
		size += s.length();
	}
	/**
	 * Makes nodes with string of data (in order) 
	 * @param cur
	 * @param s
	 * @param start
	 * @param end
	 * @return
	 */
	private Node makeInOrderNodes(Node cur, String s, int start, int end) {
		if (start > end) {
			return Node.NULL_NODE;
		}
		//middle pos at each substring determines node, + 1 for 0 start index
		int findNodePos = (start + end + 1) / 2;
		cur = new Node(s.charAt(findNodePos));
		//rank should always be size of substring to the left of the node (ie size of left sub tree)
		cur.rank = findNodePos - start;
		// recurse left and right w appropriate substrings
		cur.left = makeInOrderNodes(cur.left, s, start, findNodePos - 1);
		cur.right = makeInOrderNodes(cur.right, s, findNodePos + 1, end);
		//assign balance codes, bottom up
		if (cur.hasLeft() && cur.hasRight()) {
			cur.balance = Code.SAME;
		}
		else if (cur.hasLeft()) {
			cur.balance = Code.LEFT;
		}
		else if (cur.hasRight()) {
			cur.balance = Code.RIGHT;
		}
		else {
			cur.balance = Code.SAME;
		}
		return cur;
		
	}

	/**
	 * MILESTONE 1 return the string produced by an in-order traversal of this tree
	 */
	@Override
	public String toString() {
		// call Node toString
		StringBuilder sb = new StringBuilder();
		root.toString(sb);
		return sb.toString();
	}

	/**
	 * MILESTONE 1 Just modify the value of this.size whenever adding or removing a
	 * node. This is O(1).
	 * 
	 * @return the number of nodes in this tree, not counting the NULL_NODE if you
	 *         have one.
	 */
	public int size() {
		//call Node size
		return this.size; // nothing else to do here.
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param ch character to add to the end of this tree.
	 */
	public void add(char ch) {
		// recursively define the root as root.add() method (similar process as we have done in the past w/ insert/delete) also must increment size
		NodeInfo info = new NodeInfo(0, true);
		root = root.add(ch, size, info);
		rotationCount += info.rotationCount;
		size++;

	}

	/**
	 * MILESTONE 1
	 * 
	 * @param ch  character to add
	 * 
	 * @param pos character added in this in-order position Valid positions range
	 *            from 0 to the size of the tree, inclusive (if called with size, it
	 *            will append the character to the end of the tree). 
	 * @throws IndexOutOfBoundsException if pos is negative or too large for this
	 *                                   tree.
	 */
	public void add(char ch, int pos) throws IndexOutOfBoundsException {
		// must check for exception, highest can be size (just adding a Node to the end)
		if (pos > this.size || pos < 0) {
			throw new IndexOutOfBoundsException(); 
		}
		// recursively define the root as root.add() method (similar process as we have done in the past w/ insert/delete) also must increment size
		else {
			NodeInfo info = new NodeInfo(0, true);
			root = root.add(ch, pos, info);
			rotationCount += info.rotationCount;
			size++;
		}
		
	}

	/**
	 * MILESTONE 1 This one asks for more info from each node. You can write it
	 * similar to the arraylist-based toString() method from the BinarySearchTree
	 * assignment. However, the output isn't just the elements, but the elements AND
	 * ranks. Former students recommended that this method, while making it a little
	 * harder to pass tests initially, saves them time later since it catches weird
	 * errors that occur when you don't update ranks correctly. For the tree with
	 * root b and children a and c, it should return the string: [b1, a0, c0] There
	 * are many more examples in the unit tests.
	 * 
	 * @return The string of elements and ranks, given in an PRE-ORDER traversal of
	 *         the tree.
	 */
	public String toRankString() {
		// want a string builder to make this easier
		StringBuilder sb = new StringBuilder();
		//start brackets
		sb.append("[");
		root.toRankString(sb);
		//delete extra comma and " "
		if (sb.length() > 1) {
			sb.delete(sb.length() - 2, sb.length());
		}
		//finish brackets
		sb.append("]");
		return sb.toString();
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param pos position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException if pos is negative or too big. Note that
	 *                                   the pos is now EXclusive of the size of the
	 *                                   tree, since there is no character there.
	 *                                   But you can still use your size
	 *                                   field/method to determine this.
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		//check exceptions, cannot be equal to size here since range is 0...size - 1
		if (pos >= this.size || pos < 0) {
			throw new IndexOutOfBoundsException();
		}
		//call Node's get(pos)
		return root.get(pos);
	}

	// MILESTONE 1: They next two slow() methods are useful for testing, debugging and the
	// graphical debugger. They are the same as you used in an earlier assignment.
	// They may be O(n). 
	// Call recursive helpers in the Node class as usual.
	public int slowHeight() {
		//call Nodes height
		return root.slowHeight();
	}

	public int slowSize() {
		//call Nodes size
		return root.slowSize();
	}
	

	/**
	 * MILESTONE 1 Returns true iff (read as "if and only if") for every node in the
	 * tree, the node's rank equals the size of the left subtree. For full credit,
	 * do this in O(n) time, so in a single pass through the tree, and with only
	 * O(1) extra storage (so no temp collections). Don't use slowSize(). Instead,
	 * use the same pattern as the sum of heights problem in HW5. I put my helper
	 * class inside the Node class, but you can put it anywhere it's convenient.
	 * 
	 * PLEASE feel free to call this method (or its recursive helper) in your code
	 * while you are writing your add() method if rank isn't working correctly. You may 
	 * also modify it to print WHERE it is failing. It
	 * may be most important to use in Milestone 2, when you are updating ranks
	 * during rotations.
	 * 
	 * @return True iff each node's rank correctly equals its left subtree's size.
	 */
	public boolean ranksMatchLeftSubtreeSize() {
		// check that the count of nonmatching left subtree sizes = 0, meaning all ranks match
		return root.checkRankMatch().rankNotMatch == 0;
	}

	/**
	 * MILESTONE 2 Similar to toRankString(), but adding in balance codes too.
	 * 
	 * For the tree with root b and a left child a, it should return the string:
	 * [b1/, a0=] There are many more examples in the unit tests.
	 * 
	 * @return The string of elements and ranks, given in an pre-order traversal of
	 *         the tree.
	 */
	public String toDebugString() {
		if (this.isEmpty()) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		root.toDebugStrings(sb);
		sb.delete(sb.length() - 2, sb.length());
		sb.append("]");
		return sb.toString();
	}
	/**
	 * Simple isEmpty() method to fulfill Java's Container class 
	 * @return true if empty, else false
	 */
	private boolean isEmpty() {
		// TODO Auto-generated method stub
		return root == Node.NULL_NODE;
	}

	/**
	 * MILESTONE 2 returns the total number of rotations done in this tree since it
	 * was created. A double rotation counts as two.
	 *
	 * @return number of rotations since this tree was created.
	 */
	public int totalRotationCount() {
		 return rotationCount; // replace by a real calculation.
	}

	/**
	 * MILESTONE 2 Returns true iff (read as "if and only if") for every node in the
	 * tree, the node's balance code is correct based on its childrens' heights. For
	 * full credit, do this in O(n) time, so in a single pass through the tree, and
	 * with only O(1) extra storage (so no temp collections). Don't use
	 * slowHeight(). Instead, use the same pattern as the sum of heights problem in
	 * HW5. I put my helper class inside the Node class, but you can put it anywhere
	 * it's convenient.
	 * 
	 * The notes for ranksMatchLeftSubtreeSize() above apply here - 
	 * this method is to help YOU as the developer.
	 * 
	 * @return True iff each node's balance code is correct.
	 */
	public boolean balanceCodesAreCorrect() {
		return root.codeCorrect().errorCt == 0;
	}

	/**
	 * MILESTONE 2 Only write this one once your balance codes are correct. It will
	 * rely on correct balance codes to find the height of the tree in O(log n)
	 * time.
	 * 
	 * @return the height of this tree
	 */
	public int fastHeight() {
		return root.fastHeight();
	}

	/**
	 * MILESTONE 3
	 * 
	 * @param pos position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		// Implementation requirement:
		// When deleting a node with two children, you normally replace the
		// node to be deleted with either its in-order successor or predecessor.
		// The tests assume assume that you will replace it with the
		// *successor*.
		if (pos < 0 || pos >= size) {
			throw new IndexOutOfBoundsException();
		}
		size--;
		DeleteInfo info = new DeleteInfo();
		root = root.delete(pos, info, false);
		rotationCount += info.rotationCount;
		return info.deletedChar;
	}

	/**
	 * MILESTONE 3, EASY This method operates in O(length*log N), where N is the
	 * size of this tree.
	 * 
	 * @param pos    location of the beginning of the string to retrieve
	 * @param length length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException unless both pos and pos+length-1 are
	 *                                   legitimate indexes within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {
		if (pos < 0 || pos + length - 1 >= size) {
			throw new IndexOutOfBoundsException();
		}
		else {
			StringBuilder sb = new StringBuilder();
			for (int k = 0; k < length; k++) {
				sb.append(root.get(k + pos));
			}
			return sb.toString();
		}
	}
	
	// Feel free to add whatever other methods and helpers you need, 
	// like for the graphical debugger.
	/**
	 * Helper class for add.  Tracks the number of rotations and a boolean for when rebalancing should stop
	 * @author woodrojc
	 *
	 */
	class NodeInfo {
		int rotationCount;
		boolean shouldContinue;
		
		public NodeInfo(int rotationCount, boolean shouldContinue) {
			this.rotationCount = rotationCount;
			this.shouldContinue = shouldContinue;
		}
	}
	/**
	 * Helper class for deletion.  Tracks the deleted char (to return), number of rotations, and has a boolean to determine if rebalancing should continue
	 * @author woodrojc
	 *
	 */
	class DeleteInfo {
		int rotationCount;
		char deletedChar;
		boolean shouldContinue;
		
		public DeleteInfo() {
			this.deletedChar = '!';
			this.rotationCount = 0;
			this.shouldContinue = true;
		}
	}
	/**
	 * Method that displays the current EditTree.  Used in Graphical Debugger.
	 */
	public void show() {
		if (this.display == null) {
			this.display = new DisplayableBinaryTree(this, 960, 1080, true);
		} else {
			this.display.show(true);
		}
	}
}
