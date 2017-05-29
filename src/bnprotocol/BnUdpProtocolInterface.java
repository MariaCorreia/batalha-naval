package bnprotocol;

/**
 * BatalhaNavalProtocoloInterface
 * 
 * @author levymateus
 *
 */
public interface BnUdpProtocolInterface {
	
	/**
	 * tamanho padrão do frame o qual o servidor espera
	 */
	public static final int FRAME_SIZE = 512;

	
	/**
	 * Converte os dados (byte) para inteiro entre os indices begin e end.
	 * @param buffer - Array de byte a serem convertidos.
	 * @param begin - Indice inicial
	 * @param end - Indice final
	 * @return Inteiro referente ao dado entre begin e end.
	 */
	public static int getType(byte[] buffer, int begin, int end){
		String type = new String();
		for (int i = begin; i < end; i++) {
			type += Byte.toString(buffer[i]);
		}
		return Integer.parseInt(type);
	}
	
}
