package EntityControllers;

import java.util.ArrayList;

import Entities.AbstractProduct;
import Entities.Product;
/**
*The class MarketingEmployeeController stores information the MarketingEmpoyee user uses
* @author Biran Fridman
*/
public class MarketingEmployeeController {
	
	private AbstractProduct product;
	private ArrayList<Product> NewProductComponents = new ArrayList<>();

	public AbstractProduct getProduct() {
		return product;
	}

	public void setProduct(AbstractProduct product) {
		this.product = product;
	}

	public ArrayList<Product> getNewProductComponents() {
		return NewProductComponents;
	}

	public void setNewProductComponents(ArrayList<Product> newProductComponents) {
		NewProductComponents = newProductComponents;
	}
	
	/**
	 * ComponentsString()
	 * @return String representing the components that Marketing Employee chose for a new Product
	 */
	public String ComponentsString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for(Product product : NewProductComponents)
		{
			stringBuilder.append(product.getName() + ",");
		}
		
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		stringBuilder.append("]");
		return stringBuilder.toString();
		
	}

}
