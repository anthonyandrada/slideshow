public class CircularLinkedList implements Iterable<CaptionedImage>
{
	CaptionedImage first;
	
	public CircularLinkedList()
	{
		first =  null;
	}
	
	public CircularLinkedList(CaptionedImage head)
	{
		first = head;
	}
	
	public void add(CaptionedImage i)
	{
		if (first == null) 
			first = i;
		else if (first.next == null) 
		{
			first.next = i;
			i.previous = first;
		}
		else
		{
			CaptionedImage current = first;
			CaptionedImage last = null;
			while (current != null)
			{
				last = current;
				current = current.next;
			}
			last.next = i;
			i.previous = last;
			i.next = null;
		}
	}
	
	public CaptionedImage getFirst()
	{
		return first;
	}
	
	@Override
	public CircularIterator iterator() 
	{
		CircularIterator newIterator = new CircularIterator(getFirst());
		return newIterator;
	}
}
