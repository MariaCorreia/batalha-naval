package bnserver;

public class BnMatch {
	
	protected static int match_id = 0;
	
	protected BnUdpClientNode c1;// cliente 1
	
	protected BnUdpClientNode c2;// cliente 2
	
	public BnMatch(BnUdpClientNode c1, BnUdpClientNode c2){
		match_id++;
		this.c1 = c1;
		this.c2 = c2;
	}

}
