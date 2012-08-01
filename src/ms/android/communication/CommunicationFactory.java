package ms.android.communication;

public class CommunicationFactory {

	public static Communication com;
	
	
	public static void loadCom(boolean friendFinder){
		
		if(friendFinder){
			com = new FriendFinder();
		}
		else{
		//	com = new FindHotels();
		}
	}
}
