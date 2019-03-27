package test.interiale.prospect;

import fr.interiale.ws.prospect.exception.IteWebServiceParrainageException;
import fr.interiale.ws.prospect.service.GestionParrainageService;

public class TestParrain {

	public static void main(String[] args) {
		
		
		try {
			String retVal = GestionParrainageService.checkCodeParrainage("UK654");
			System.out.println(retVal);
		} catch (IteWebServiceParrainageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
