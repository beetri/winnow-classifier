package ia2.winnow;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import weka.core.Instance;

public class WinnowUtil {
	
	public static int[] getNotZeroAttributeIndex(Instance instance) {
		List<Integer> attributeIndex = new LinkedList<Integer>();
		double[] instanceVector = instance.toDoubleArray();
		for(int i=0; i<instanceVector.length; i++)
			if(instanceVector[i] > 0)
				attributeIndex.add(i);
		return revertToArray(attributeIndex);
	}

	private static int[] revertToArray(List<Integer> integerList) {
		int[] result = new int[integerList.size()];
		Iterator<Integer> iterator = integerList.iterator();
		for(int i=0; i<result.length; i++)
			result[i]=iterator.next();
		return result;
	}
	
}
