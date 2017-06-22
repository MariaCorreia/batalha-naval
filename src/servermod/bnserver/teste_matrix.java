package servermod.bnserver;

public class teste_matrix {

	public static void main(String[] args) {
		String str = "22#levy#A1#B3#E8";
		String split[] = str.split("#");
		System.out.println("cliente enviou uma matriz");
		String string = null;
		for (int i = 2; i < split.length; i++) {
			string += "#" + split[i];
			
		}
		System.out.println(string);
		BnMatrix m = new BnMatrix();
		m.initialize(string);
		
		
		System.out.println("search="+m.getCoord("A1"));
		m.setCoordValue("A1", 1);
		System.out.println("search="+m.getCoord("A1"));
		
		int feedback = m.hitOnCoord("B3");
		feedback = m.hitOnCoord("A1");
		feedback = m.hitOnCoord("E8");
		
		
		if(feedback == BnMatrix.HIT){
			System.out.println("hit");
		}else if(feedback == BnMatrix.MISS){
			System.out.println("miss");
		}else if(feedback == BnMatrix.EMPTY_MATRIX){
			System.out.println("matriz vazia");
		}
				
	}

}
