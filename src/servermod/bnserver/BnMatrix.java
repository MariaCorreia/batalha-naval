package servermod.bnserver;

/**
 *  Responável por realizar todas as funções relacionadas a matriz de navios do jogo
 */
public class BnMatrix {
	
	static final int MISS = -100;
	
	static final int  HIT = 100;
	
	static final int BAD_COORD = -200;
	
	static final int EMPTY_COORD = 0;
	
	static final int DESTROYED = 200;
	
	static final int EMPTY_MATRIX = 300;
	
	private int [][] matrix;
	
	private int count;
	
	BnMatrix (){
		count = 0;
	}

	public int[][] getMatriz() {
		return matrix;
	}

	public void setMatriz(int[][] matriz) {
		this.matrix = matriz;
	}
	
	protected boolean initialize (String matrix){
		
		try {
			this.matrix = new int[10][10];
			String split[] = matrix.split("#");
			
			/*for (int i = 0; i < split.length; i++) {
				System.out.println(":"+split[i]);
			}*/
			
			for (int index = 1; index < split.length; index++) {
				char ch = split[index].charAt(0);
				int i = (int) ch - (int) 64;
				ch = split[index].charAt(1);
				int j = (int) ch - (int) 48;
				//System.out.println(i + j);
				this.matrix[i][j] = 1;
				count++;
				System.out.println("[" + i + "]" + "[" + j + "]" + "=" + this.matrix[i][j]);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param argi - linha
	 * @param argj - coluna
	 * @return valor contido na posição ij da matriz e BAD_COORD em caso de erro
	 */
	public int getCoord (String arg){
		try {
			int i = (int) arg.charAt(0) - (int) 64;
			int j = (int) arg.charAt(1) - (int) 48;
			return this.matrix[i][j];
		} catch (Exception e) {
			return BAD_COORD;
		}		
	}
	
	/**
	 * 
	 * @param cood - String do tipo A1 .. H8 correspondente a coordenada. 
	 * @param value - valor a inserir na coordenada
	 * @return 0 para sucesso e BAD_COORD para o contrário.
	 * @see BAD_COORD
	 */
	protected int setCoordValue(String coord, int value){
		try {
			int i = (int) coord.charAt(0) - (int) 64;
			int j = (int) coord.charAt(1) - (int) 48;
			this.matrix[i][j] = value; 
			System.out.println("set=["+i+"]["+j+"]="+this.matrix[i][j]);
		} catch (Exception e) {
			return BAD_COORD;
		}
		return 0;
	}
	
	/**
	 * Testa posição da matriz. Em caso de HIT set a posição para DESTROYED. 
	 * @param coord - coordenada do tiro
	 * @return HIT se acertou algo; 
	 * MISS se a coordenada estava vazia; 
	 * EMPTY caso a  matrix esteja zerada.
	 * BAD_COORD em caso de erro ou nenhum das alternativas anteriores for satisfeita.
	 */
	protected int hitOnCoord(String coord){
		try {
			int i = (int) coord.charAt(0) - (int) 64;
			int j = (int) coord.charAt(1) - (int) 48;
			if(this.matrix[i][j] != EMPTY_COORD){
				
				this.matrix[i][j] = DESTROYED;
				count--;
				
				if(count <= 0 ) 
					return EMPTY_MATRIX;
				
				return HIT;
				
			}else if(this.matrix[i][j] == EMPTY_COORD){
				// coordenada vazia, não faz nada . . .
				return MISS;
			}else if(this.matrix[i][j] == DESTROYED){
				// peça destruida, não faz nada . . .
				return DESTROYED;
			}
			
		} catch (Exception e) {
			return BAD_COORD;
		}
		return BAD_COORD;
	}

}
