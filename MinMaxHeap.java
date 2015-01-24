import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeMap;

/**
 * Min and Max exchanges level
 * in this priority queue/heap.
 * 
 * @author Huy
 * @param <E>
 * @version 4/13/2014
 */
public class MinMaxHeap<E extends Comparable<? super E>>
{
    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    private int size;
    private Object[] heap;
    private Comparator comparator = null;

    /**
     * 
     */
    public MinMaxHeap()
    {
        heap = new Object[DEFAULT_INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * 
     * @param c
     */
    public MinMaxHeap(Collection<? extends E> c)
    {
        int i = 0;
        heap = (E[]) new Object[c.size()*2 + 1];
        for(E item : c)
        {
            heap[i] = item;
            i++;
        }
        //build heap???
    }

    /**
     * 
     * @param initialCapacity
     */
    public MinMaxHeap(int initialCapacity)
    {
        if (initialCapacity <  1)
        {
            throw new IllegalArgumentException();
        }
        heap = (E[]) new Object[initialCapacity];
        size = 0;
    }

    
    /**
     * 
     * @param initialCapacity
     * @param comparator
     */
    public MinMaxHeap(int initialCapacity, Comparator<? super E> comparator)
    {
        if (initialCapacity <  1)
        {
            throw new IllegalArgumentException();
        }
        this.heap = (E[]) new Object[initialCapacity];
        this.size = 0;
        this.comparator = comparator;
    }

    /**
     * 
     * @param c
     */
    public MinMaxHeap(MinMaxHeap<? extends E> c)
    {
        this.heap = (E[]) new Object[c.heap.length];       
        this.size = c.size;                               // Copy size
        this.comparator = (Comparator< ? super E >) c.comparator;
        for(int i = 1; i <= c.size(); i++)
        {                
            this.heap[i] = c.heap[i];                       // reference
        }
    }

    /**
     * 
     * @param c
     */
    public MinMaxHeap(SortedSet<? extends E> c)
    {
        this.comparator = (Comparator< ? super E >) c.comparator();
    }

    /**
     * 
     * @return
     */
    public Comparator< ? super E > comparator()
    {
        return comparator;
    }

    /**
     * 
     */
    public void clear() 
    {
        for (int i = 0; i <= size; i++)
        {
            heap[i] = null;
        }
        size = 0;
    }

    /**
     * 
     * @return
     */
    public Iterator<E> iterator() 
    {
        return iterator();
    }

    /**
     * 
     * @return
     */
    public int size()
    {
        return this.size;
    }

    /**
     * 
     * @param element
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean add(E element)
    {
        if(size == heap.length)
        {
            return true;
        }
        int p = (size + 1) / 2;
        if(size == 0)
        {
            heap[1] = element;
            size++;
            return true;
        }
        switch(level(p))
        {
            case 1:
                if(myCompareTo(element, (E) heap[p]) < 0)
                {
                    heap[size+1] = heap[p];
                    perUpMin(p, element);
                }
                else
                {
                    perUpMax(p, element);
                }
                break;
            case 2:
                if(myCompareTo(element, (E) heap[p]) > 0)
                {
                    heap[size+1] = heap[p];
                    perUpMax(p, element);
                }
                else
                {
                    perUpMin(p, element);
                }
                break;
        }
        return true;
    }
    
    /**
     * 
     * @param element
     * @return
     */
    public boolean offer(E element)
    {
        return add(element);
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public E pollMin()
    {
        if(size == 0)
        {
            return null;
        }
        
        E m = (E) heap[1];
        E x = (E) heap[size];
        heap[size] = null;
        int i, last;
        //the last node may replace it
        //then find the location of x
        for(i = 1, last = size / 2; i <= last; )   
        //this means node_i has children, so we needn't check whether n == 1;
        {
            int minIndex = MinChildGrandC(i);
            if(myCompareTo(x,(E) heap[minIndex]) <= 0) 
            //this means x is the smallest node, 
            //so we will use it to succeed the root,
            //that is , node_i(just as we do below);  
            {
                break;
            }
            //else we use node_k to succeeed the subroot i, for k is the smallest of the sub_heap
            heap[i] = heap[minIndex];
            if(minIndex <= 2*i + 1)  
            //then we check the node_minIndex's identity,
            //either a child of node_i or a grandchild of it;
            {
                i = minIndex;  //when is a child, that means there isn't any more level below, 
                               //so the place minIndex is suitable for x
                break;  //no more child below
            }
     
            int parent = minIndex / 2;  
            //if a grandchild, may it violate the min_max_heap property,
            //that is, item.key may bigger than heap[parent].key
            E tmp;
            if(myCompareTo(x, (E) heap[parent]) > 0)
            {
                tmp = (E) x;
                x = (E) heap[parent];
                heap[parent] = tmp;
            }
            i = minIndex;   // then it become a sub_problem of the pre_problem, 
                            // which has a root being a grandchild of the pre_root
        }
        heap[i] = x;
        return m;
    }
    
    /**
     * 
     * @return
     */
    @SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
    public E pollMax()
    {
        if(size == 0)
        {
            return null;
        }
     
        if(size == 1)
        {
            return (E) heap[size--];
        }
     
        int i = indexMax();
        
        E m = (E) heap[size];
        E x = (E) heap[size--];
        
        for(int last = size / 2; i <= last; )
        {
            int maxIndex = MaxChildGrandC(i);
            if(myCompareTo(x, (E) heap[maxIndex]) >= 0)
            {
                break;
            }
            heap[i] = heap[maxIndex];
            if(maxIndex <= 2*i+1)
            {
                i = maxIndex;
                break;
            }
     
            int parent = maxIndex/2;
            
            if(myCompareTo(x, (E) heap[parent]) < 0)
            {
                E tmp = (E) x;
                x = (E) heap[parent];
                heap[parent] = tmp;
            }
            i = maxIndex;
        }
        heap[i] = x;
        return m;
    }
    
    /**
     * 
     * @param i
     * @param element
     */
    public void perDownMin(int i, E element)
    {
        while((2 * i) < size)
        {
            int minIndex = MinChildGrandC(i);
            if((minIndex == i*2) || (minIndex == i*2 + 1))
            {
                if (myCompareTo((E) heap[minIndex], (E) heap[i]) < 0)
                {
                    E tem = (E) heap[minIndex];
                    heap[minIndex] = (E) heap[i];
                    heap[i] = tem;
                }
                return;
            }
            else
            {
                if (myCompareTo((E) heap[minIndex], (E) heap[i]) > 0)
                {
                    return;
                }
                E temp = (E) heap[minIndex];
                heap[minIndex] = (E) heap[i];
                heap[i] = temp;
                int parentI = minIndex/2;
                if (myCompareTo((E) heap[minIndex], (E) heap[parentI]) > 0)
                {
                    E tmp = (E) heap[minIndex];
                    heap[minIndex] = (E) heap[parentI];
                    heap[parentI] = tmp;
                }
                i = minIndex;
            }              
        }
    }

    /**
     * 
     * @param i
     * @param element
     */
    public void perDownMax(int i, E element)
    {
        while((2 * i) < size)
        {
            int maxIndex = MaxChildGrandC(i);
            if((maxIndex == i*2) || (maxIndex == i*2 + 1))
            {
                if (myCompareTo((E) heap[maxIndex], (E) heap[i]) > 0)
                {
                    E tem = (E) heap[maxIndex];
                    heap[maxIndex] = (E) heap[i];
                    heap[i] = tem;
                }
                return;
            }
            else
            {
                if (myCompareTo((E) heap[maxIndex], (E) heap[i]) < 0)
                {
                    return;
                }
                E temp = (E) heap[maxIndex];
                heap[maxIndex] = (E) heap[i];
                heap[i] = temp;
                int parentI = maxIndex/2;
                if (myCompareTo((E) heap[maxIndex], (E) heap[parentI]) < 0)
                {
                    E tmp = (E) heap[maxIndex];
                    heap[maxIndex] = (E) heap[parentI];
                    heap[parentI] = tmp;
                }
                i = maxIndex;
            }              
        }
    }
    
    /**
     * 
     * @param index1
     * @param index2
     * @return
     */
    public boolean grandChild(int index1, int index2)
    {
        return ((index2 == 8 * index1 + 1) || (index2 == 8 * index1));
    }

    public int MinChildGrandC(int i)
    {
        E min = (E) heap[2*i];
        int child = 2 * i;
        int index = -1;
        while (child <= size)
        {
            if( myCompareTo(min, (E) heap[child]) > 0) 
            {
                min = (E) heap[child];
                index = i;
            }
            child++;
        }  
        return index;
    }
    
    /**
     * 
     * @param i
     * @return
     */
    public int MaxChildGrandC(int i)
    {
        E max = (E) heap[2*i];
        int child = 2 * i;
        int index = -1;
        while (child <= size)
        {
            if( myCompareTo(max, (E) heap[child]) < 0) 
            {
                max = (E) heap[child];
                index = i;
            }
            child++;
        }  
        return index;
    }

    /**
     * 
     * @param i
     * @param element
     */
    public void perUpMax(int i, E element)
    {
        for (int gp = i /  4; myCompareTo(element, (E) heap[gp]) > 0; gp /= 4) 
        {
            heap[i] = heap[gp];
            i = gp;
        }
        heap[i] = element;
    }

    /**
     * 
     * @param i
     * @param element
     */
    public void perUpMin(int i, E element)
    {
        for (int gp = i /  4; myCompareTo(element, (E) heap[gp]) < 0; gp /= 4) 
        {
            heap[i] = heap[gp];
            i = gp;
        }
        heap[i] = element;
    }

    /**
     * 
     * @param i
     * @return
     */
    public int level(int i)
    {
        int n = (int) (Math.log(i) / Math.log(2));
        if(n % 2 == 1)
        {
            return 2;   //max level
        }
        return 1;  //min level
    }

    /**
     * 
     * @param element
     * @return
     */
    public boolean contains(E element)
    {
        if(find(element) == -1)
        {
            return false;
        }
        return true;
    }

    /**
     * 
     * @param element
     * @return
     */
    @SuppressWarnings("unchecked")
    public int find(E element)
    {
        for (int i = 1; i <= heap.length; i++)
        {
            if(myCompareTo((E)heap[i], element) == 0)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * 
     * @return
     */
    public E peekMin()
    {
        if(size < 1)
        {
            return null;
        }
        else
        {
            return (E) heap[1];
        }
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public E peekMax()
    {
        if(size < 1)
        {
            return null;
        }
        else if(size == 1)
        {
            return (E) heap[1];
        }
        else if(size == 2)
        {
            return (E) heap[2];
        }
        else
        {
            if(myCompareTo((E) heap[2], (E) heap[3]) > 0)
            {
                return (E) heap[2];
            }
            return (E) heap[3];
        }
    }
    
    /**
     * 
     * @return
     */
    public int indexMax()
    {
        int i = 2; 
        E x = (E) heap[2];
        if(size >= 3)
        {
            if(myCompareTo((E) heap[2],(E) heap[3]) > 0)
                x = (E) heap[2];
            else
            {
                x = (E) heap[3];
                i = 3;
            }
        }
        return i;
    }

    /**
     * 
     * @param x
     * @param y
     * @return
     */
    private int myCompareTo(E x, E y)
    {
        if(comparator == null)
        {
            return x.compareTo(y);
        }
        else
        {
            return comparator.compare(x, y);
        }
    }

    /**
     * 
     * @return
     */
    public Object[] toArray()
    {
        Object[] a = new Object[size];
        System.arraycopy(heap, 0, a, 0, size);
        return a;
    }

}
