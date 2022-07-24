package project.FPT.flightapp;

public interface Heap {
/*
  Interface: The Heap will provide this collection of operations:

    insert
      in: a FlightOrder object, containing FlightOrder entities field, with Price being determinant
          We may have duplicate Prices being inserted 
      return: void
      effect: the heap size goes up one
    delMin
      in: nothing
      return: nothing
      effect: the heap size goes down one, the element at the root is removed
    getMin
      in: nothing
      return: a FlightOrder object (the one in slot 1 on the array)
      effect: no change in heap state
    size
      in: nothing
      return: integer 0 or greater, the count of the number of elements in the heap
      effect: no change in heap state
  */

  // ADT operations
  void insert(FlightOrder elt);
  void delMin();
  FlightOrder getMin();  
  int size();
  FlightOrder[] getHeap();
}