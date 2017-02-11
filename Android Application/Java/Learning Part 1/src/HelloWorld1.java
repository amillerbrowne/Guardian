
//public class HelloWorld1 {
//	int age;
//	public void Dog(){}
//	
//	public void DogA(int DogAge){
//		age = DogAge;
//	}
//	
//	private void pritL(){
//		System.out.println("the puppies age is: " );
//	}
//	
//	public static void main(String[] args) {
//		int num=9;
//		Dog Spike = new DogA(5);
//		Spike.pritL();
//	}
//
//}
class HelloWorld1  {
  
  int age;

	public HelloWorld1(int dogsAge) {
    
  	age = dogsAge;
    
  }
  public void bark(){
    System.out.println("Woof!");
  }

	public static void main(String[] args) {
    
	 HelloWorld1 Spike = new HelloWorld1(5);
    Spike.bark();

	}

}