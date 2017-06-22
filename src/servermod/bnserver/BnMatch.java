package servermod.bnserver;

public class BnMatch {
	
	protected static int match_id = 0;
	
	protected BnUdpClientNode player_1;
	
	protected BnUdpClientNode player_2;
	
	protected boolean isPalaying;
		
	public BnMatch(BnUdpClientNode c1, BnUdpClientNode c2){
		match_id++;
		this.player_1 = c1;
		this.player_2 = c2;
		this.isPalaying = false;
	}

}
