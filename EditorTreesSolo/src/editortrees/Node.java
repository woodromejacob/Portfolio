package editortrees;
import editortrees.EditTree.DeleteInfo;

/**
 * A node in a height-balanced binary tree with rank. Except for the NULL_NODE,
 * one node cannot belong to two different trees.
 * 
 * @author woodrojc
 */
public class Node {

	enum Code {
		SAME, LEFT, RIGHT;

		// Used in the displayer and debug string
		public String toString() {
			switch (this) {
			case LEFT:
				return "/";
			case SAME:
				return "=";
			case RIGHT:
				return "\\";
			default:
				throw new IllegalStateException();
			}
		}
	}

	// The fields would normally be private, but for the purposes of this class,
	// we want to be able to test the results of the algorithms in addition to the
	// "publicly visible" effects

	char data;
	Node left, right; // subtrees
	int rank; // inorder position of this node within its own subtree.
	Code balance;
	DisplayableNodeWrapper displayableNodeWrapper;

	// Feel free to add other fields that you find useful.
	// You probably want a NULL_NODE, but you can comment it out if you decide
	// otherwise.
	// The NULL_NODE uses the "null character", \0, as it's data and null children,
	// but they could be anything since you shouldn't ever actually refer to them in
	// your code.
	static final Node NULL_NODE = new Node('\0', null, null);
	// Node parent; You may want parent, but think twice: keeping it up-to-date
	// takes effort too, maybe more than it's worth.
/**
 * 
 * @param data
 * @param left
 * @param right
 * Constructor for the Node class
 */
	public Node(char data, Node left, Node right) {
		//set field to params, went ahead and set rank to 0. And balance to = for later use 
		this.data = data;
		this.left = left;
		this.right = right;
		this.rank = 0;
		this.balance = Code.SAME;
		this.displayableNodeWrapper = new DisplayableNodeWrapper(this);
	}
	/**
	 * Constructor for the Node class.  I made this to help with shortening lines of code in the EditTree(EditTree) constructor. 
	 * @param data
	 * @param rank
	 * @param code
	 */
	public Node(char data, int rank, Code code) {
		//set field to params, went ahead and set rank to 0. And balance to = for later use 
		this.data = data;
		this.left = NULL_NODE;
		this.right = NULL_NODE;
		this.rank = rank;
		this.balance = code;
	}
/**
 * Constructor for the node class that only takes in the data.  Calls other constructor to set its children.
 * @param data
 * Constructor for Node class, calls other constructor
 */
	public Node(char data) {
		//given
		this(data, NULL_NODE, NULL_NODE);
	}
/**
 * Returns the in-order traversal of the nodes as a string
 * @return string
 */
	public void toString(StringBuilder sb) {
		//stop when hit a null_node
		if (this == NULL_NODE) {
			return;
		}
		else {
			// in order is L,N,R so follow that order in return
			this.left.toString(sb);
			sb.append(this.data);	
			this.right.toString(sb);
		}
		
	}

	/**
	 * Adds a new node with the specified data in the specified position
	 * @param ch
	 * @param pos
	 * @return root
	 */
	public Node add(char ch, int pos, EditTree.NodeInfo info) {
		//return new Node with the char if we find where to insert it
		if (this == NULL_NODE) {
			return new Node(ch);
		}
		// as in video, compare position and rank
		// if pos <= rank we need to go left and NOT adjust position, but we want to increase the rank of every node we
		//go left on since the rank is size of left subtree
		else if (pos <= this.rank) {
			this.rank++;
			this.left = this.left.add(ch, pos, info);
			return updateCodesFromLeftInsert(this, info);
		}
		//rank > pos means go right, we need to adjust position since we skipped a node and all the nodes to the left of it (its rank)
		// we need to recursively call as usual, but with our adjusted position
		else {
			this.right = this.right.add(ch, pos - this.rank - 1, info);
			return updateCodesFromRightInsert(this, info);
		}
	}
		/**
		 * Double Left Rotate Method. Takes care of both rotations and updates balance codes.
		 * @param parent
		 * @param child
		 * @param grandChild
		 * @return parent
		 */
		private Node doubleLeftRotate(Node parent, Node child, Node grandChild) {
			//must store the grandchilds code, this is how to determine resulting balance codes 
			Code grandChildCode = grandChild.balance;
			//now perform the two rotations, we need right then left
			child = singleRightRotate(child, grandChild);
			parent = singleLeftRotate(parent, child);
			//case 1: grandchild was balanced, everything remains balanced
			if (grandChildCode.equals(Code.SAME)) {
				parent.left.balance = Code.SAME;
				parent.right.balance = Code.SAME;
			}
			//case 2: grandchild tipped left, left of parent gets balanced but right side is tipped right as it gained the taller subtree 
			else if (grandChildCode.equals(Code.LEFT)) {
				parent.left.balance = Code.SAME;
				parent.right.balance = Code.RIGHT;
			}
			//case 3: grandchild tipped right, left of parent gets longer subtree, its side is tipped left, right side is balanced.
			else if (grandChildCode.equals(Code.RIGHT)) {
				parent.left.balance = Code.LEFT;
				parent.right.balance = Code.SAME;
			}
			return parent;
			
	}
		/**
		 * Double Right Rotate Method. Takes care of both rotations and updates balance codes.
		 * @param parent
		 * @param child
		 * @param grandChild
		 * @return parent
		 */
		private Node doubleRightRotate(Node parent, Node child, Node grandChild) {
			//similar to double left, track grandchilds code
			Code grandChildCode = grandChild.balance;
			//do left then right
			child = singleLeftRotate(child, grandChild);
			parent = singleRightRotate(parent, child);
			//case 1: grandchild was balanced, everything remains balanced
			if (grandChildCode.equals(Code.SAME)) {
				parent.left.balance = Code.SAME;
				parent.right.balance = Code.SAME;
			}
			//case 2: grandchild tipped left, left of parent gets balanced but right side is tipped right as it gained the taller subtree 
			else if (grandChildCode.equals(Code.LEFT)) {
				parent.left.balance = Code.SAME;
				parent.right.balance = Code.RIGHT;
			}
			//case 3: grandchild tipped right, left of parent gets longer subtree, its side is tipped left, right side is balanced.
			else if (grandChildCode.equals(Code.RIGHT)) {
				parent.left.balance = Code.LEFT;
				parent.right.balance = Code.SAME;
			}
			return parent;	
	}
		/**
		 * Only used in delete method so I can pass it the info object for the edge case (test 385)
		 * @param parent
		 * @param child
		 * @param info
		 * @return child
		 */
		private Node singleRightRotateDelete(Node parent, Node child, EditTree.DeleteInfo info) {
			//checks first condition for an edge case
			boolean edgeCase = false; 
			if (child.hasLeft() && child.hasRight() && parent.right == NULL_NODE) {
				edgeCase = true;
			}
			//normal right rotate
			Node temp = child.right;
			child.right = parent;
			parent.left = temp;
			parent.rank = temp == NULL_NODE ? 0 : parent.rank - child.rank - 1;
			parent.balance = Code.SAME;
			child.balance = Code.SAME;
			//second edge case condition
			if (parent.left.hasLeft() && parent.left.hasRight() && parent.hasRight() && !parent.right.hasLeft() && !parent.right.hasRight()) {
				child.balance = Code.RIGHT;
				parent.balance = Code.LEFT;
				info.shouldContinue = false;
			}
			if (edgeCase) {
				child.balance = Code.RIGHT;
				parent.balance = Code.LEFT;
				info.shouldContinue = false;
			}
			return child;
		}	
	/**
	 * Single Right Rotate method. Used in re-balancing
	 * @param parent
	 * @param child
	 * @return
	 */
	private Node singleRightRotate(Node parent, Node child) {
		//mirror single left
		Node temp = child.right;
		child.right = parent;
		parent.left = temp;
		parent.rank = temp == NULL_NODE ? 0 : parent.rank - child.rank - 1;
		parent.balance = Code.SAME;
		child.balance = Code.SAME;
		return child;
	}
	/**
	 * Single Left Rotate.  Used in re-balancing
	 * @param parent
	 * @param child
	 * @return
	 */
	private Node singleLeftRotate(Node parent, Node child) {
		//used code from ICQ13
		Node temp = child.left;
		int tempRank = parent.rank + child.rank;
		child.left = parent;
		parent.right = temp;
		parent.balance = Code.SAME;
		child.balance = Code.SAME;
		child.rank = tempRank + 1;
		return child;
	}
	/**
	 * Makes a string including the rank of each node, in pre-order (N,L,R)
	 * @param sb
	 */
	public void toRankString(StringBuilder sb) {
		//stop at NULL_NODES
		if (this == NULL_NODE) {
			return;
		}
		//add node to the string, then go left first, then right to get preorder
		else {
			sb.append(this.data);
			sb.append(this.rank + ", ");
		}
		this.left.toRankString(sb);
		this.right.toRankString(sb);
	}

	/**
	 * Find's the data at the specified position
	 * @param pos
	 * @return data
	 */
	public char get(int pos) {
		//need to find where rank equals position
		if (this.rank == pos) {
			return this.data;
		}
		//similar to add we don't wanna change position if we go left
		else if (pos < this.rank) {
			//go left
			return this.left.get(pos);
		}
		else {
			//go right
			return this.right.get(pos - this.rank - 1);
		}
	}

	/**
	 * Compute's the trees height
	 * @return height
	 */
	public int slowHeight() {
		//return -1 for null_node
		if (this == NULL_NODE) {
			return -1;
		}
		//return math max of left and right height (just as in previous assignments)
		else {
			return 1 + Math.max(this.left.slowHeight(), this.right.slowHeight());
		}
	}

	/**
	 * Compute's the trees size
	 * @return size
	 */
	public int slowSize() {
		//0 for a NULL_NODE
		if (this == NULL_NODE) {
			return 0;
		}
		//1 for this plus left size and right size 
		else {
			return 1 + this.left.slowSize() + this.right.slowSize();
		}
	}

	/**
	 * Verify's that all ranks match the left subtree
	 * @return RankMatch
	 */
	public RankMatch checkRankMatch() {
		//return a new RankMatch, set fields to 0
		if (this == NULL_NODE) {
			return new RankMatch(0, 0);
		}
		//recursively call the method on the left in right (i referenced HW5 for this problem)
		RankMatch leftRM = this.left.checkRankMatch(); 
		RankMatch rightRM = this.right.checkRankMatch();
		int leftSubSize = leftRM.numNodes;
		//check if it equals rank
		if (this.rank != leftSubSize) {
			return new RankMatch(leftSubSize + 1, leftRM.rankNotMatch + 1);
		}
		else {
			return new RankMatch(leftSubSize + 1 + rightRM.numNodes, leftRM.rankNotMatch);
		}
	}	

	/**
	 * Inner RankMatch class 
	 * @author woodrojc
	 * Helper class for checkRankMatch
	 */
	class RankMatch {
		//counts nodes
		int numNodes;
		//counter for non matching ranks
		int rankNotMatch;
		/**
		 * Constructor for RankMatch
		 * @param numNodes
		 * @param rankNotMatch
		 */
		public RankMatch(int numNodes, int rankNotMatch) {
			this.numNodes = numNodes;
			this.rankNotMatch = rankNotMatch;
		}
	}


	/**
	 * Creates a debug string that has the data, rank, and code included for each node.
	 * Pre-order traversal.
	 * @param sb
	 */
	public void toDebugStrings(StringBuilder sb) {
		if (this == NULL_NODE) {
			sb.append("");
		}
		else {
			sb.append(this.data);
			sb.append(this.rank);
			sb.append(this.balance);
			sb.append(", ");
			this.left.toDebugStrings(sb);
			this.right.toDebugStrings(sb);
		}
	}
	/**
	 * Fast-Height Method, using balance codes.
	 * @return
	 */
	public int fastHeight() {
		//return -1 if null
		if (this == NULL_NODE) {
			return -1;
		}
		//if Code.Same pick a side, i chose left
		else if (this.balance.equals(Code.SAME) || this.balance.equals(Code.LEFT)) {
			return 1 + this.left.fastHeight();
		}
		else {
			return 1 + this.right.fastHeight();
		}
	}
	/**
	 * Checks to assure all balance codes are correct.
	 * @return true if all codes correct, else false
	 */
	public CodeCorrect codeCorrect() {
		//return -1 for null node height
		if (this == NULL_NODE) {
			return new CodeCorrect(-1, 0);
		}
		//recurse left and right
		CodeCorrect leftHeight = this.left.codeCorrect();
		CodeCorrect rightHeight = this.right.codeCorrect();
		//on way back up, assure heights are all off by maximum of 1, and assure appropriate codes are in place
		if (leftHeight.height + 1 == rightHeight.height && this.balance.equals(Code.RIGHT)) {
			return new CodeCorrect(1 + rightHeight.height, leftHeight.errorCt + rightHeight.errorCt);
		}
		else if (leftHeight.height - 1 == rightHeight.height && this.balance.equals(Code.LEFT)) {
			return new CodeCorrect(1 + leftHeight.height, leftHeight.errorCt + rightHeight.errorCt);
		}
		else if (leftHeight.height == rightHeight.height && this.balance.equals(Code.SAME)) {
			return new CodeCorrect(1 + leftHeight.height, leftHeight.errorCt + rightHeight.errorCt);
		}
		else {
			return new CodeCorrect(1 + Math.max(leftHeight.height,  rightHeight.height), leftHeight.errorCt + rightHeight.errorCt + 1);
		}
		
	}
	/**
	 * Code correct helper class used in CodesCorrect() method. Tracks error count and the height
	 * @author woodrojc
	 *
	 */
	class CodeCorrect {
		int errorCt;
		int height;
		
		public CodeCorrect(int height, int errorCt) {
			this.errorCt = errorCt;
			this.height = height;
		}
	}
	/**
	 * Used in graphics debugger
	 * @return true if has a left node, false else
	 */
	public boolean hasLeft() {
		if (left==NULL_NODE || left==null) {
			return false;
		}
		return true;
	}
	/**
	 * Used in graphics debugger
	 * @return true if has a right node, false else
	 */
	public boolean hasRight() {
		if (right==NULL_NODE|| right==null) {
			return false;
		}
		return true;
	}
	/**
	 * Used in graphics debugger
	 * @return false, didn't use parent pointers
	 */
	public boolean hasParent() {
		return false;
	}
	/**
	 * Used in graphics debugger
	 * @return NULL_NODE, didn't use parent pointers
	 */
	public Node getParent() {
		return NULL_NODE;
	}
	/**
	 * Hibbard Deletion but with height re-balancing. 2 children case uses in order successor.
	 * @param pos
	 * @param info
	 * @param deletingSuccessor
	 * @return root
	 */
	public Node delete(int pos, DeleteInfo info, boolean deletingSuccessor) {
		//if rank = pos, found node to delete
		if (this.rank == pos) {
			if (!deletingSuccessor) {
				info.deletedChar = this.data;
			}
			//2 kid case
			if (this.hasLeft() && this.hasRight()) {
				Node replace = findInOrderSuccessor(this.right);
				this.data = replace.data;
				this.right = this.right.delete(pos - this.rank, info, true);	
				return updateCodesFromRightDelete(this, info);
			}//1kid
			else if (this.hasLeft() || this.hasRight()) {
				return left == NULL_NODE ? right : left;
			}//leaf
			else {
				return NULL_NODE;
			}
		}
		// similar to add and get, move left do not change pos param
		else if (pos < this.rank) {
			//subtract rank when delete from left subtree
			this.rank--;
			this.left = this.left.delete(pos, info, false);
			return updateCodesFromLeftDelete(this, info);
		}
		else if (pos > this.rank) {
			//now we change the pos param
			this.right = this.right.delete(pos - this.rank - 1, info, false);
			return updateCodesFromRightDelete(this, info);	
		}
		return this;
	}
	/**
	 * Helper method to update codes on way up from left during deletion.
	 * @param node
	 * @param info
	 * @return this
	 */
	private Node updateCodesFromLeftDelete(Node node, DeleteInfo info) {
		//detect for rotation, DONT stop in deletion
		if (this.balance.equals(Code.RIGHT) && info.shouldContinue) {
			if (this.right.balance.equals(Code.LEFT)) {
				info.rotationCount += 2;
				return doubleLeftRotate(this, this.right, this.right.left);
			}
			else {
				info.rotationCount++;
				return singleLeftRotate(this, this.right);
			}
		}
		//switch to right, we know the balance above will not be affect so stop looking
		else if (this.balance.equals(Code.SAME) && info.shouldContinue) {
			this.balance = Code.RIGHT;
			info.shouldContinue = false;
		}
		else {
			if (info.shouldContinue) {
				this.balance = Code.SAME;
			}
		}
		return this;
	}
	/**
	 * Helper method to update balance codes and perform rotations coming up from right on delete
	 * @param node
	 * @param info
	 * @return this
	 */
	private Node updateCodesFromRightDelete(Node node, DeleteInfo info) {
		//rotations
		if (this.balance.equals(Code.LEFT) && info.shouldContinue) {
			if (this.left.balance.equals(Code.RIGHT)) {
				info.rotationCount += 2;
				return doubleRightRotate(this, this.left, this.left.right);
			}
			else {
				info.rotationCount++;
				return singleRightRotateDelete(this, this.left, info);
			}
		}
		//know rebalancing should stop if same --> left
		else if (this.balance.equals(Code.SAME) && info.shouldContinue) {
			this.balance = Code.LEFT;
			info.shouldContinue = false;
		}
		else {
			if (info.shouldContinue) {
				this.balance = Code.SAME;
			}
		}
		return this;
	}
	/**
	 * Helper method to update balance codes on way up from left on insert
	 * @param node
	 * @param info
	 * @return this
	 */
	private Node updateCodesFromLeftInsert(Node node, EditTree.NodeInfo info) {
		//check for rotations, if rotate stop rebalancing
		if (this.balance.equals(Code.LEFT) && info.shouldContinue) {
			if (this.left.balance.equals(Code.RIGHT)) {
				info.rotationCount += 2;
				info.shouldContinue = false;
				return doubleRightRotate(this, this.left, this.left.right);
			}
			else {
				info.rotationCount++;
				info.shouldContinue = false;
				return singleRightRotate(this, this.left);
			}
		}
		//if same and inserted left, change to left, keep going up
		if (this.balance.equals(Code.SAME) && info.shouldContinue) {
			this.balance = Code.LEFT;
		}
		//rebalances to same, stop checking
		else if (this.balance.equals(Code.RIGHT) && info.shouldContinue) {
			info.shouldContinue = false;
			this.balance = Code.SAME;
		}
		return this;
	}
	/**
	 * Helper method to update balance codes and check for rotation when coming up from right during insert
	 * @param node
	 * @param info
	 * @return this
	 */
	private Node updateCodesFromRightInsert(Node node, EditTree.NodeInfo info) {
		//check rotations
		if (this.balance.equals(Code.RIGHT) && info.shouldContinue) {
			if (this.right.balance.equals(Code.LEFT)) {
				info.rotationCount += 2;
				info.shouldContinue = false;
				return doubleLeftRotate(this, this.right, this.right.left);
			}
			else {
				info.rotationCount++;
				info.shouldContinue = false;
				return singleLeftRotate(this, this.right);
			}
		}
		//if same and inserted to right, change to right, keep going
		if (this.balance.equals(Code.SAME) && info.shouldContinue) {
			this.balance = Code.RIGHT;
		}
		//rebalance, stop moving up
		else if (this.balance.equals(Code.LEFT) && info.shouldContinue) {
			info.shouldContinue = false;
			this.balance = Code.SAME;
		}
		return this;
	}
	/**
	 * Helper method to find the inorder successor for hibbard deletion
	 * @param node
	 * @return in order successor
	 */
	public Node findInOrderSuccessor(Node node) {
		while (node.left != NULL_NODE) {
			node = node.left;
		}
		return node;
	}


}