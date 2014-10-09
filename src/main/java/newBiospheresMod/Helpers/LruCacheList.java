package newBiospheresMod.Helpers;

import java.util.LinkedList;

import akka.japi.Creator;
import akka.japi.Predicate;

public class LruCacheList<T>
{
	public final int TotalItems;

	public LruCacheList(int totalItems)
	{
		this.TotalItems = totalItems;
	}

	private LinkedList<T> _backingList = new LinkedList<T>();

	public synchronized void Push(T item)
	{
		_backingList.remove(item);
		_backingList.push(item);
	}

	public synchronized boolean Contains(T item)
	{
		return _backingList.contains(item);
	}

	public synchronized T FindOrAdd(Predicate<T> predicate, Creator<T> creator)
	{
		boolean first = true;

		if (predicate != null)
		{
			for (T item: _backingList)
			{
				if (predicate.test(item))
				{
					if (!first)
					{
						_backingList.remove(item);
						_backingList.push(item);
					}
					return item;
				}

				first = false;
			}
		}

		T item = null;

		try
		{
			if (creator != null)
			{
				item = creator.create();
				_backingList.push(item);

				while (_backingList.size() > TotalItems)
				{
					_backingList.removeLast();
				}
			}
		}
		catch (Throwable ignoreMe)
		{ /* do nothing */}

		return item;
	}
}