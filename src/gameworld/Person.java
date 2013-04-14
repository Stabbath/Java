package gameworld;

public class Person extends EconUnit {
    int movSpeed, movesDone;           //moves per turn; how many moves have been used this turn
    
    public boolean Move(World world, int[] direction, int amount) {
    	int x = position[0] + direction[0];
        int y = position[1] + direction[1];
        if (world.IsInMap(x, y)) {
            Sector bufLoc = world.GetSector(x, y);
            bufLoc.freeSpace += size*amount;   //block space in new location
            world.GetSector(position[0], position[1]).freeSpace -= size*amount;
            if (bufLoc.freeSpace > size*amount && amount <= quantity && movesDone < movSpeed ) {
                Person pers = (Person) bufLoc.GetUnitOfTypeOwnedBy(type, owner);
                if (amount == quantity) {	//moving everything
                    RemoveFromSector(world.GetSector(position[0], position[1]));	//remove because we moved everyone in the unit
                	if (pers != null) {		//just add the quantity
                		pers.quantity += amount;
                	} else {	//actually move the unit
                        position[0] = x;              //move unit
                        position[1] = y;              //
                        AddToSector(bufLoc);
                	}
                } else {	//only moving part of them
                	if (pers != null) {		//just add the quantity
                		pers.quantity += amount;
                	} else {	//create a new unit
                		new Person(type, world, owner, x, y, amount);
                	}
            		quantity -= amount;	//remove amount from the previous sector regardless
                }
                	//pers.movesDone++;
                    //commented out for movement testing since turns aren't implemented yet                movesDone++;                //increment number of moves done this turn
                return true;
            }
        }
        return false;
    }

    public String getInfo() {
    	return owner + " " + type + " " + quantity;
    }
    
    public Person(int itype, World world, int player, int x, int y, int quant) {
        foodCost = 1;   woodCost = 0;
        size = 1;		quantity = quant;
        movSpeed = 1;

        int[] foo = {x, y};
        
        owner = player;
        position = foo;
        type = itype;

        switch (itype) {
            case Base.PERSON_WORKER: {
                size = 1;
                foodCost = 1;   woodCost = 0;
                movSpeed = 2;
            }
        }
        
        this.AddToSector(world.GetSector(x, y));
    }
}