package project.FPT.flightapp;

public class MinBinHeap implements Heap {
	private FlightOrder[] array; // load this array
	private int size; // how many items currently in the heap
	private int arraySize; // Everything in the array will initially
							// be null. This is ok! Just build out
							// from array[1]
	private int insertIndex;

	public MinBinHeap(int nelts) {
		this.array = new FlightOrder[nelts + 1]; // remember we dont use slot 0
		this.arraySize = nelts + 1;
		this.size = 0;
		this.array[0] = null; // 0 not used, so this is arbitrary

		this.insertIndex = 1;
	}

	// Please do not remove or modify this method! Used to test your entire Heap.
	@Override
	public FlightOrder[] getHeap() {
		return this.array;
	}

	@Override
	public void insert(FlightOrder elt) {
		// TODO Auto-generated method stub
		if(insertIndex > arraySize - 1) {
			return;
		}
		this.array[insertIndex] = elt;
		insertIndex++;
		size++;
		
		int utilityIndex = insertIndex;  //value for index comparing

		FlightOrder temp = elt;

		/*
		 * Insert at end of index
		 * Then bubble up, swapping with parent if price is lower
		 */
		while (utilityIndex != 1) {
			FlightOrder tempParent = this.array[utilityIndex / 2];
			if (temp.getPrice() >= tempParent.getPrice()) {
				break;
			} else {
				this.array[utilityIndex] = tempParent;
				utilityIndex = utilityIndex / 2;
			}
		}

		this.array[utilityIndex] = elt;
	}

	@Override
	public void delMin() {
		// TODO Auto-generated method stub
		if (size == 0) {
			return;
		}

		if (size() == 1) {
			this.array[1] = null;
			size--;
			insertIndex = 1;
			return;
		}
		// Put final elements to the top and set the former index to null
		this.array[1] = this.array[insertIndex - 1];

		FlightOrder temp = this.array[1];
		this.array[insertIndex - 1] = null;

		insertIndex--;
		size--;

		int utilityIndex = 1;  //value for index comparing, set at 1 intially to bubble down

		if (array[utilityIndex * 2] == null) {
			return;
		}
		while (array[utilityIndex * 2] != null) {
			FlightOrder x;
			FlightOrder tempLChild = this.array[utilityIndex * 2];
			int swapIndex = 0;

			//compare left and right child to choose which to swap
			x = tempLChild;
			if (utilityIndex * 2 + 1 <= arraySize - 1) {
				FlightOrder tempRChild = this.array[utilityIndex * 2 + 1];

				if (tempRChild != null) {
					x = (tempLChild.getPrice() <= tempRChild.getPrice()) ? tempLChild : tempRChild;
					swapIndex = (tempLChild.getPrice() <= tempRChild.getPrice()) ? (utilityIndex * 2) : (utilityIndex*2 + 1);
				} else {
					x = tempLChild;
				}
			}

			//if elements is already smaller than child then stop
			if (temp.getPrice() <= x.getPrice()) {
				break;
			} else {
				this.array[utilityIndex] = x;
				utilityIndex = swapIndex;
				/*
				 * int childIndex = x.getSlot(); x.setSlot(temp.getSlot());
				 * temp.setSlot(childIndex);
				 */
			}

			if (utilityIndex * 2 > arraySize - 1) {
				break;
			}
		}

		this.array[utilityIndex] = temp;

	}

	@Override
	public FlightOrder getMin() {
		// TODO Auto-generated method stub
		return this.array[1];
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.size;
	}
	// ===============================================================
	//
	// here down you implement the ops in the interface
	//
	// ===============================================================


}
