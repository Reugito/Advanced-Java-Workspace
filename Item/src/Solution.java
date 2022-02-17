import java.util.*;
public class Solution {

	public static void main(String[] args) {
		List<Item> itemList = new ArrayList<>();
		Scanner sc = new Scanner(System.in);
		int itemCount = sc.nextInt();
		for(int i=1; i<= itemCount; i++) {
			int id = sc.nextInt();
			int quantity = sc.nextInt();
			String name = sc.nextLine();
			int price = sc.nextInt();
			Item obj = new Item(id, quantity, name, price);
			itemList.add(obj);
		}
		
		
		Solution.findItemWithMaximumPrice(itemList);
	}

	private static Item findItemWithMaximumPrice(List<Item> itemList) {
		Item maxPrice = itemList
			      .stream()
			      .max(Comparator.comparing(Item::getPrice))
			      .orElseThrow(NoSuchElementException::new);
		return maxPrice;
	}

}
