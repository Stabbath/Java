package gameworld;

public class Player {
    int id;
    String name = new String(new char[32]);
    String pass = new String(new char[32]);
    
    int food;
    int wood;
    
    public Player(int index) {
        id = index;
        name = "";
        food = 0;
        wood = 0;
    }
    
    public boolean setInfo(String newname, String newpass, int index, int newfood, int newwood) {
    	if (name.equals("")) {
    		name = newname;
    		pass = newpass;
    		id = index;
    		food = newfood;
    		wood = newwood;
    		
    		return true;
    	}
    	return false;
    }
    
    public String getInfo() {
    	return name + " " + pass + " " + id + " " + food + " " + wood;
    }
}
