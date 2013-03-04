Java
====

A learning experience. Decided to learn java, and to do so I'll be making a game. 
Will probably be messy and have silly things but hey I'm new to all of its features.

To-do:
  General
    round-end progress on production, movement, attacks, etc for every unit in the world
      idea 1: loop through every location, inside of each location loop through all of the people and all the structures of that location, inside of the structure loop through all its people, tell it to do things
      idea 2: put units who have been given orders or are in production or are doing something that takes some turns to do on a list or stack, and then just loop through that list/stack
    store received commands until all players have done their moves or the turn's duration has expired, and then execute one for each player through each cycle based on which ones were received first for each cycle
  Players
    eveything
  Units
    unit training/structure building
    method to return resource collection from buildings based on their workers
  Refine random map generation
    more customisation, even division of resources, more realism, etc


Reminders:
  Have a lot of different resources later on, with different workers for each resource buildings and different ways to get resources, for example:
    Farm:
      Bonus food production on normal/fertile land
        Worker: Farmer
    Hunter's Lodge:
      Gatherer: Hunter (gathers animal corpses)
      Bonus food production in woods/forest
        Worker: Butcher (provides food from animals)
      Bonus leather/fur production
        Worker: Tanner (provides leather from animals)
    Fishing Wharf:
      Gatherer: Fisherman
      Food production from water tiles
        Worker: Fish Gutter
