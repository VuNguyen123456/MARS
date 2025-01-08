/**
 * This is a Line class.
 * @author Vu Nguyen
 * @param  <T> type of the line
 */
class Line<T> {
	//you MUST use this field for credit on your assignment
	//if you change the name of the type, you will lose a
	//_lot_ of points, don't do it!
	/**
	 * data.
	 */
	private T[] data;
	
	/**
	 * Constructor.
	 */
	@SuppressWarnings("unchecked")
	public Line() {
		//default for data should be capacity of 1
		//data = (T[]) new Object[1];
		initializeData(1);
		//Note for @SuppressWarnings("unchecked")
		//you may add this to any _helper_ methods for _this_
		//method, but you may not add it to other methods
		//in this project
		
		//O(1)
	}
	/**
	 * Initialize the data.
	 * @param size of the data
	 */
	@SuppressWarnings("unchecked")
	private void initializeData(int size){
		data = (T[]) new Object[size];
	}
	/**
	 * double the size of data if needed.
	 */
	private void doubleDataSize(){
		@SuppressWarnings("unchecked")
		T[] temp = (T[]) new Object[data.length * 2];
		for(int i = 0; i< data.length; i++){
			temp[i] = data[i];
		}
		data = temp;
	}
	/**
	 * get the data at provided index.
	 * @param index of the data
	 * @return hi
	 */
	public T get(int index) {
		//should throw appropriate exception if index is out of bounds
		//for this action (see main method tester for this file)
		if (index >= data.length || index < 0 || data[index] == null){
			throw new IndexOutOfBoundsException("Out of bouund"); // might need to throw exception somehow
		}
		//returns the value at the given index
		return data[index];
		//O(1)
	}
	/**
	 * remove the data ad the provided index.
	 * @param index of removed data
	 * @return the removed data
	 */
	public T remove(int index) {
		//should throw appropriate exception if index is out of bounds
		//for this action (see main method tester for this file)
		if (index >= data.length || index < 0){
			throw new IndexOutOfBoundsException("Out of bouund"); // might need to throw exception somehow
		}
		T removed = data[index];
		for(int i = 0; i < data.length - 1; i++){
			// if(i == data.length-1){
			// 	data[data.length - 1] = null;
			// 	break;
			// }
			if(i >= index){
				data[i] = data[i+1];
			}
		}
		data[data.length -1] = null;
		return removed;
		//removed the object at the given index from the line
		//later objects move forward in the line
		//returns the value removed
		
		//O(n)
	}
	/**
	 * Add the item at provided index in the line.
	 * @param item to add
	 * @param index to add
	 */
	@SuppressWarnings("unchecked")
	public void add(T item, int index) {
		//should throw appropriate exception if index is out of bounds
		//for this action (see main method tester for this file)
		if (index > data.length || index < 0 || index > getSize()){
			throw new IndexOutOfBoundsException("Out of bouund");
		}
		int count = getSize();
		if(count + 1 > data.length){
			doubleDataSize();
		}
		for(int i = data.length - 1; i > 0; i--){
			if(i > index){
				data[i] = data[i-1];
			}
		}
		data[index] = item;
		//Note for @SuppressWarnings("unchecked")
		//you may add this to any _helper_ methods for _this_
		//method, but you may not add it to other methods
		//in this project
		
		//adds item to the given index, shifting values over
		//should double the data array side if more space is
		//needed
		//O(n)
	}
	/**
	 * Get the size of the line.
	 * @return the size
	 */
	public int getSize() {
		int count = 0;
		for(int i = 0 ; i < data.length;i++){
			if(data[i] == null){
				continue;
			}
			count += 1;
		}
		//O(1)
		
		return count;
	}
	/**
	 * Get the capacity of the line.
	 * @return the capacity
	 */
	public int getCapacity() {
		//O(1)
		
		return data.length; //replace this default return!
	}
	/**
	 * This is the main.
	 * @param args for user
	 */
	public static void main(String[] args) {
		//This is a main-method tester.
		//You may alter/change/remove this method.
		
		//It doesn't test everything, but it should give you an idea of
		//the types of scenarios you should be testing when writing this
		//class. The JUnit tests for grading will test a lot more things!
		
		Line<Integer> test = new Line<>();
		if(test.getSize() == 0 && test.getCapacity() == 1) {
			System.out.println("yay 1");
		}
		
		test.add(1, 0);
		if(test.getSize() == 1 && test.getCapacity() == 1 && test.get(0) == 1) {
			System.out.println("yay 2");
		}
		
		test.add(2, 0);
		if(test.getSize() == 2 && test.getCapacity() == 2 && test.get(0) == 2 && test.get(1) == 1) {
			System.out.println("yay 3");
		}
		
		test.add(3, 2);
		if(test.getSize() == 3 && test.getCapacity() == 4 && test.get(0) == 2 && test.get(1) == 1 && test.get(2) == 3) {
			System.out.println("yay 4");
		}
		
		test.remove(2);
		if(test.getSize() == 2 && test.getCapacity() == 4 && test.get(0) == 2 && test.get(1) == 1) {
			System.out.println("yay 5");
		}
		
		test.remove(0);
		if(test.getSize() == 1 && test.getCapacity() == 4 && test.get(0) == 1) {
			System.out.println("yay 6");
		}
		
		try {
			test.add(4, 2);
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println("yay 7");
		}
		
		try {
			test.remove(-1);
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println("yay 8");
		}
		
		test.remove(0);
		try {
			test.get(0);
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println("yay 9");
		}
	}
}
