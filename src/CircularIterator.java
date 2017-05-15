import java.util.Iterator;

	public class CircularIterator implements Iterator<CaptionedImage>
	{
		CaptionedImage first;
		CaptionedImage current;
		
		public CircularIterator(CaptionedImage i)
		{
			first = i;
			current = first;
		}
		
		public void remove()
		{
			CaptionedImage curr = first;
			CaptionedImage temp = null;
			while (curr.next != current)
			{
				temp = curr;
				curr = curr.next;
			}
			temp.next = curr.next;
			current = temp;
		}
		
		public CaptionedImage next()
		{
			if (hasNext())
				current = current.next;
			else
				current = first;
			CaptionedImage image = current;
			return image;
		}
		
		public boolean hasNext()
		{
			if (current.next != null) 
				return true;
			else
				return false;
		}
	
		public CaptionedImage previous()
		{
			if (hasPrevious())
				current = current.previous;
			else 
				while (hasNext())
					current = current.next;
			CaptionedImage image = current;
			return image;
		}
		
		public boolean hasPrevious()
		{
			if (current.previous != null) 
				return true;
			else 
				return false;
		}
	}