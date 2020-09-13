/*
 Name: Marcus Lee
 Email: leemarcusmz@brandeis.edu
 Bugs: None
 Description: This class is for the AVL node within the tree. 
 */
public class AVLPlayerNode{
    private Player data;
    private double value;
    private AVLPlayerNode parent;
    private AVLPlayerNode leftChild;
    private AVLPlayerNode rightChild;
    private int rightWeight;
    private int balanceFactor;
    private int height = 0;
    
    public AVLPlayerNode(Player data,double value){
    //Constructors for the data and value field
    	this.data = data;
    	this.value = value;
    }

    public AVLPlayerNode insert(Player newGuy,double value){
    //This method returns the new root of the tree, updates the balance factor and right weight, and also uses rotation to maintain the AVL condition
    	AVLPlayerNode a = new AVLPlayerNode(newGuy, value); //new node that is going to be inserted
    	AVLPlayerNode current = this; //new current node that is "this"
    	AVLPlayerNode beforeCurrent = null; //new beforeCurrent node which will save the current before any changes was implemented on it
    	
    	while(current != null){
    	//this loop searches where the new node should be inserted
    		current.setHeight(current.getHeight()+1);
			beforeCurrent = current;
    		if(value <= current.getValue()){
    			current = current.getLeftChild();
    		}else{
    			current = current.getRightChild();
    		}
    	}
    	a.setParent(beforeCurrent);
    	//inserting node a, where a's parent will be set
    	if(value <= beforeCurrent.getValue()){ //if statements to determine whether a is the left or right child of the parent node
    		beforeCurrent.setLeftChild(a);
    	}else{
    		beforeCurrent.setRightChild(a);
    	}
    	
    	current = this; //resetting current to this, to begin balancing
    	AVLPlayerNode root = this; //initializing node root
    	if(heightBalance(current) ==-2){
    		if(current.getRightChild().getLeftChild() == null){
    		//single left rotation
    			root = rotateLeft(current);
    		}else{
    			root = rotateRight(current.getRightChild());
    			root = rotateLeft(current);
    			//right-left rotation
    		}
    	}else if(heightBalance(current) ==2){
    		if(current.getLeftChild().getLeftChild() != null){
    		//single left rotation
    			root = rotateRight(current);
    		}else{
    			root = rotateLeft(current.getLeftChild());
    			root = rotateRight(current);
    			//left-right rotation
    		}
    	} 	
    	return root;
    }
    
    public AVLPlayerNode delete(double value){
    //This method returns the new root of the tree and deletes the node with the given value in the parameter
    	AVLPlayerNode current = this; //initializing current node that is this
    	while(current.getValue() != value){ //loop to find which node has the value given, the node that will be deleted
    		if(current.getValue() < value){
    			current = current.getRightChild();
    		}else{
    			current = current.getLeftChild();
    		}
    	}
    	if(current.getLeftChild() != null){ //while loop to delete node, reconnecting child and parents
    		current.getLeftChild().setParent(current.getParent());
    		if(current.getParent().getLeftChild() == current){
        		current.getParent().setLeftChild(current.getLeftChild());
    		}else{
        		current.getParent().setRightChild(current.getLeftChild());
    		}
    	}else{
    		current.getRightChild().setParent(current.getParent());
    		if(current.getParent().getLeftChild() == current){
        		current.getParent().setLeftChild(current.getRightChild());
    		}else{
        		current.getParent().setRightChild(current.getRightChild());
    		}
    	}
    	return null;
    }
    
    private AVLPlayerNode rotateRight(AVLPlayerNode current){
    //right rotation method, location of rotation is determined by the current
    	AVLPlayerNode newCurrent = current.getLeftChild(); 
    	//making newCurrent the left node child of current for further positioning purposes

    	if(current.getParent() != null){ //determines if the node that will be rotated has parent or not
    		newCurrent.setParent(current.getParent());
    		if(current.getParent().getLeftChild() == current){
    			current.getParent().setLeftChild(newCurrent);
    		}else{
    			current.getParent().setRightChild(newCurrent);
    		}
    	} 		
    	current.setParent(newCurrent); //setting new parent
    	newCurrent.setRightChild(current); //setting new child
    	current.setLeftChild(null); //setting the end node's right and left children to be null
    	current.setRightChild(null);
    	if(this == current){
    		return newCurrent;
    	}else{
    		return this;
    	}
    }
    
    private AVLPlayerNode rotateLeft(AVLPlayerNode current){
    //left rotation, besides the connection of parents and children, which are complete opposites, everything else is the same as right rotation
    	AVLPlayerNode newCurrent = current.getRightChild();
    	
    	if(current.getParent() != null){
    		System.out.println(newCurrent.getData().getName());
    		newCurrent.setParent(current.getParent());
    		if(current.getParent().getLeftChild() == current){
    			current.getParent().setLeftChild(newCurrent);
    		}else{
    			current.getParent().setRightChild(newCurrent);
    		}
    	} 		
    	current.setParent(newCurrent);
    	newCurrent.setLeftChild(current);
    	current.setLeftChild(null);
    	current.setRightChild(null);
    	if(this == current){
    		return newCurrent;
    	}else{
    		return this;
    	}
    }

    public Player getPlayer(double value){
    //returns data of the Player with the given value within the parameter
    	AVLPlayerNode current = this;
    	while(current.getValue() != value){ 
    	//searches for value within the tree, goes left or right depending on whether the value is greater or less than
        	if(value > current.getValue()){
        		current = current.getRightChild();
        	}else{
        		current = current.getLeftChild();
        	}
    	}
    	return current.getData();
    }
    
    public int getRank(double value){
    //returns the rank of the player of given value
    	AVLPlayerNode current = this;
    	int rank = 1; //rank begins at 1, and adds on afterwards
    	while(current.getValue() != value){
    	//searches for the node
    		if(current.getValue() < value){
    			current = current.getRightChild();
    		}else{
    			current = current.getLeftChild();
    		}
    	}
    	return rankCount(current, rank, value); //method of finding rank
    }
    public int rankCount(AVLPlayerNode current, int rank, double value){
    //recursive that depends on where the node is, the search begins at where the node is, and then keeps on adding 1, until it passes through all the nodes before reaching the node with rank 1/highest elo score, which is to the very right of the tree
    	if(current == current.getParent().getLeftChild()){
    		return rankCount(current.getParent(), rank++, value);
    	}
    	if((current.getRightChild() != null) && (current.getLeftChild().getValue() > value) && (current.getLeftChild() != null)){
    		return rankCount(current.getRightChild(), rank++, value) + rankCount(current.getLeftChild(), rank++, value);
    	}
    	if(current.getRightChild() != null){
    		return rankCount(current.getRightChild(), rank++, value);
    	}
    	if(current.getLeftChild() != null){
        	if((current.getLeftChild().getValue() > value) && (current.getLeftChild() != null)){
        		return rankCount(current.getLeftChild(), rank++, value);
        	}
    	}
    	return rank;
    }
 
    public String treeString(){
    //this method returns the tree of names with parentheses separating subtrees
    	AVLPlayerNode current = this;
    	String treeString = "";
    	String treeStringFinal = "("+inOrder(current, treeString)+")"; //String equals the String method that is called
    	return treeStringFinal;
    }
    public String inOrder(AVLPlayerNode current, String treeString){
    //a recursive method that travels through the entire tree, using the inOrder method that will be called in treeString, returns treeString
    	if((current.getLeftChild() == null) && (current.getRightChild() == null)){
    		return current.getData().getName();
    	}
    	if((current.getLeftChild() != null) &&(current.getRightChild() != null)){
    		return "("+inOrder(current.getLeftChild(), "("+treeString)+")"+current.getData().getName()+"("+inOrder(current.getRightChild(), treeString+")")+")";
    	}
    	if(current.getLeftChild() != null){
    		return "("+inOrder(current.getLeftChild(), "("+treeString)+")"+current.getData().getName();
    	}
    	if(current.getRightChild() != null){
    		return current.getData().getName()+")"+inOrder(current.getRightChild(), treeString+")");
    	}
    	return treeString;
    }
    
    public String scoreboard(){
    //this method should return a formatted scoreboard in descending order of value
    	AVLPlayerNode current = this;
    	String scoreBoard = "";
    	System.out.println("Name    ID SCORE");
    	return reverseInOrder(current, scoreBoard); //calls method for retrieving information for scoreBoard
    }
    public String reverseInOrder(AVLPlayerNode current, String scoreBoard){
    //method that is a recurrsive, which is the reverse of Inorder, for scoreboard, where it visits the right side first instead of left
    	if(current.getRightChild() != null){
    		scoreBoard = reverseInOrder(current.getRightChild(), scoreBoard);  //+current.getData().getName()+"   "+current.getData().getID()+" "+current.getData().getELO()+"\n";
    	}
    	scoreBoard += current.getData().getName()+"        "+current.getData().getID()+" "+current.getData().getELO()+"\n"; 
    	if(current.getLeftChild() != null){
    		scoreBoard = reverseInOrder(current.getLeftChild(), scoreBoard);//current.getData().getName()+"   "+current.getData().getID()+" "+current.getData().getELO()+"\n"+
    	}
    	return scoreBoard;
    }
    public double getValue(){ //returns value
    	return value;
    }
    public Player getData(){ //returns data
    	return data;
    }
    public AVLPlayerNode getLeftChild(){ //returns leftChild
    	return leftChild;
    }
    public AVLPlayerNode getRightChild(){ //returns rightChild
    	return rightChild;
    }
    public AVLPlayerNode getParent(){ //returns parent
    	return parent;
    }
	public void setParent(AVLPlayerNode node){ //sets parent
		parent = node;
	}
	public void setLeftChild(AVLPlayerNode node){ //sets leftChild
		leftChild = node;
	}
	public void setRightChild(AVLPlayerNode node){ //sets rightChild
		rightChild = node;
	}
	public int getHeight(){ //returns height
		return height;
	}
	public void setHeight(int a){ //sets height
		height = a;
	}
	public int height(AVLPlayerNode current){ 
	//returns value for the height of either left or right subtree
		if(current == null){
			return -1;
		}else{
			return current.getHeight();
		}
	}
	public int heightBalance(AVLPlayerNode root){
	//returns an int that determines whether the tree is balanced or not
		int balance = height(root.getLeftChild())- height(root.getRightChild());
		//if left side is taller, then balance will be 2, else -2, if balanced then it will be 1/ 0/ -1
		return balance;
	}
}
    
	
