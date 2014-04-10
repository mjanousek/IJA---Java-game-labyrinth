/**
 * Testy na funkcnost souboru.
 * @author xjanou14 Martin Janoušek, xfiala46 Marek Fiala
 * @version 1.2
 */
package ija.project.test;

import static org.junit.Assert.*;
import ija.project.io.FileReader;
//import ija.homework3.io.Console;
import ija.project.objects.*;
import ija.project.table.*;
import ija.project.figure.*;


import org.junit.After;
import org.junit.Before;

import org.junit.Test;

public class JunitTest {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }
    /**
     * Test na funkcnost vytvareni instanci tridy TableObject.
     */
    @Test
    public void testTableObject01() {
        TableObject to1 = TableObject.create("W");	//wall
        TableObject to2 = TableObject.create("G");	//gate
        TableObject to3 = TableObject.create("K");	//key
        TableObject to4 = TableObject.create("F");	//finish
        TableObject to5 = TableObject.create(".");	//anything else

        assertEquals("Object to1 je instanci tridy Wall.", Wall.class, to1.getClass());
        assertEquals("Object to2 je instanci tridy Gate.", Gate.class, to2.getClass());
        assertEquals("Object to3 je instanci tridy Key.", Key.class, to3.getClass());
        assertEquals("Object to1 je instanci tridy Finish.", Finish.class, to4.getClass());
        assertNull("Object to5 nebyl vytvoren.", to5);

        assertFalse("Zed nelze otevrit", to1.canBeOpen());
        assertTrue("Zavrenou branu lze otevrit", to2.canBeOpen());
        to2.open();
        assertFalse("Otevrenou branu nelze otevrit", to2.canBeOpen());
    }

    /**
     * Test na obsazovani polecek a skakani na jine policka.
     */
    @Test
    public void testTable02() {
    	//test policek a pohybu mezi nimi
    	Table t1 = new Table(5,5);
        t1.insertLine("WWWWW");
        t1.insertLine("W.F.W");
        t1.insertLine("W.W.W");
        t1.insertLine("W.G.W");
        t1.insertLine("WWWWW");
        
        TableField f = t1.fieldAt(0,0);
        assertFalse("Pozice 0,0: nelze obsadit.", f.canSeize());
        f = t1.fieldAt(1, 1);
        assertTrue("Pozice 1,1: lze obsadit.", f.canSeize());
        assertEquals("Posun na pozici 2 1.", t1.fieldAt(2,1), f = f.fieldOnPosition(2,1));
        assertTrue("Pozice 2 1: lze obsadit.", f.canSeize());
        assertEquals("Posun na pozici 2 2.", t1.fieldAt(2,2), f = f.fieldOnPosition(2,2));
        assertFalse("Pozice 2 2: nelze obsadit.", f.canSeize());
        assertEquals("Posun na pozici 1 3.", t1.fieldAt(3,1), f = f.fieldOnPosition(3,1));
        assertTrue("Pozice 3 1: lze obsadit.", f.canSeize());
        assertEquals("Posun na pozici 3 2.", t1.fieldAt(3,2), f = f.fieldOnPosition(3,2));
        assertFalse("Pozice 3 2: nelze obsadit - zavrena brana.", f.canSeize());
        
    }

    /**
     * Test na rotaci hrace.
     */
    @Test
    public void testPlayer03() {
    	//test policek a rotace hrace
    	System.out.println("testPlayer03");
    	Table t1 = new Table(5,5);
        t1.insertLine("WWWWW");
        t1.insertLine("W.F.W");
        t1.insertLine("W.W.W");
        t1.insertLine("W.G.W");
        t1.insertLine("WWWWW");
        
        char sight, before;
        Player h1;
        TableField f1;
        
        
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        h1 = t1.createPlayer();
        assertNotNull("Hrac 1 vytvorena.", h1);
        f1 = h1.seizedField();
        assertNotNull("Hrac ma ulozene pole.", f1);
        
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        before = sight = h1.symbolSight();
        h1.rotateLeft();
        System.out.print("LEFT: ");
        assertFalse("Hrac se otocil doleva.", sight == h1.symbolSight());
        System.out.println("ok");
        
        t1.printTable();
        sight = h1.symbolSight();
        System.out.print("LEFT: ");
        h1.rotateLeft();
        System.out.println("ok");
        assertFalse("Hrac se otocil podruhe doleva.", sight == h1.symbolSight());
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        sight = h1.symbolSight();
        h1.rotateRight();
        System.out.print("RIGHT: ");
        assertFalse("Hrac se otocil doprava.", sight == h1.symbolSight());
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        sight = h1.symbolSight();
        h1.rotateRight();
        System.out.print("RIGHT: ");
        assertFalse("Hrac se otocil podruhe doprava.", sight == h1.symbolSight());
        assertEquals("Hrac se diva do puvodniho smeru.", before, h1.symbolSight());
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
    }

    /**
     * test provadejici primitivni pruchod hrace polem. Testuje se otaceni, posun hrace a zda
     * lze otevrit branu.
     */
    @Test
    public void testPlayer04() {
    	//test policek a rotace a posunu hrace
    	System.out.println("testPlayer04");
        Table t1 = new Table(5,5);
        t1.insertLine("WWWWW");
        t1.insertLine("W...W");
        t1.insertLine("W.G.W");
        t1.insertLine("W...W");
        t1.insertLine("WWWWW");
        
        TableField f = t1.fieldAt(0,0);
        
        //umisten hrac na policku
        Player pl = new Player(1,1, f, 2);
		f.seize(pl);
		assertNotNull("Hrac byl vytvoren.", pl);
		t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
		
		System.out.print("STEP: ");
		pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (1)", f.positionCol(), 1);
        assertEquals("Je ve stejnem radku (2)", f.positionRow(), 2);
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        //hrac se posune dolu
        System.out.print("STEP: ");
        pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (1)", f.positionCol(), 1);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        //hrac se otoci a posune doprava
        System.out.print("LEFT: "); 
        pl.rotateLeft();
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        System.out.print("STEP: ");
        pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (2)", f.positionCol(), 2);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        //hrac zkousi otevrit branu
        System.out.print("LEFT: ");
        pl.rotateLeft();
        assertFalse("Nemuze vejit do brany", pl.move());
        assertEquals("Je ve stejnem sloupci (2)", f.positionCol(), 2);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        //hraci je pridelen klic a otevre branu
        pl.addKeys(1);
        System.out.print("OPEN: ");
        assertTrue("Otevrela se brana",pl.openGate());
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        System.out.print("STEP: ");
        assertTrue("Vejde do brany", pl.move());
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (2)", f.positionCol(), 2);
        assertEquals("Je ve stejnem radku (2)", f.positionRow(), 2);
            
    }

    /**
     * Test slozitejsiho pruchodu hrace polem. Hrac prochazi, sebere klic,
     * otevre branu a dojde do cile.
     */
    @Test
    public void testPlayer05() {
    	//test policek hrace herniho jadra(pohyb, rotace, otevreni, sebrani klicu, finish)
    	System.out.println("testPlayer05");
    	
    	Table t1 = new Table(5,5);
        t1.insertLine("WWWWW");
        t1.insertLine("W.WFW");
        t1.insertLine("W.WGW");
        t1.insertLine("W..KW");
        t1.insertLine("WWWWW");
        
        
        TableField f = t1.fieldAt(0,0);
        
        //umisten hrac na policku
        Player pl = new Player(1,1, f, 2);
		f.seize(pl);
		assertNotNull("Hrac byl vytvoren.", pl);
		t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
		
		//posune se dolu
        System.out.print("STEP: ");
        pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (1)", f.positionCol(), 1);
        assertEquals("Je ve stejnem radku (2)", f.positionRow(), 2);
        System.out.println("ok");
        t1.printTable();
        System.out.println("");
        
        //posune se dolu
        System.out.print("STEP: ");
        pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (1)", f.positionCol(), 1);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        System.out.print("LEFT, STEP: ");
        //otoci a posune se doprava
        pl.rotateLeft();
        pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (2)", f.positionCol(), 2);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        System.out.println("ok ");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");

        System.out.print("STEP: ");
        assertFalse("Nemuze obsadit klic", pl.move());
        assertEquals("Je ve stejnem sloupci (2)", f.positionCol(), 2);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        System.out.print("TAKE: ");
        //sebere klic
        assertTrue("Vzali jsme klic",pl.takeKey());
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        System.out.print("STEP: ");
        pl.move();
        pl.rotateLeft();
        System.out.println("ok");
        
        //otevre branu
        System.out.print("OPEN: ");
        assertTrue("Otevrela se brana",pl.openGate());
        assertTrue("Vejde do brany", pl.move());
        System.out.println("ok");
        
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        f = pl.seizedField();
        //vstoupi do otevrene brany
        assertEquals("Je ve stejnem sloupci (3)", f.positionCol(), 3);
        assertEquals("Je ve stejnem radku (2)", f.positionRow(), 2);
        
        assertFalse("Neni vitez!", pl.isWinner());
        //zvitezi
        System.out.print("MOVE, WINNER: ");
        assertTrue("Vejde na finish.", pl.move());
        assertTrue("Je vitez!", pl.isWinner());
        System.out.println("ok");
        t1.printTable();
        //v ramci formatovani a lepsi prehlednosti
        System.out.println("");
        
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (3)", f.positionCol(), 3);
        assertEquals("Je ve stejnem radku (1)", f.positionRow(), 1);
        
        
    }
    
    @Test
    public void testFileReader(){
    	//Test nacitani souboru a tisknuti spravnych znaku zastupujici objekty v bludisti
    	System.out.println("testFileReader");
        
    	FileReader fr = new FileReader();
    	assertNotNull("fr neni nullovy", fr);
    	Table t;
    	
    	assertNotNull("Existujici soubor.", t = fr.openFile("maze2"));
    	t.printTable();
    	System.out.println("");
        
    	TableField f = t.fieldAt(0,0);
    	assertEquals("Na policku 0,0 je zed.", f.printObj(), 'W');
    	f = f.fieldOnPosition(1,1);
    	assertEquals("Na policku 1,1 je vzduchoprazdno(neni objekt).", f.printObj(), '.');
    	f = f.fieldOnPosition(1,2);
    	assertEquals("Na policku 1,2 je brana.", f.printObj(), 'G');
    	f = f.fieldOnPosition(1,3);
    	assertEquals("Na policku 1,3 je cil.", f.printObj(), 'F');
    	f = f.fieldOnPosition(3,12);
    	assertEquals("Na policku 1,3 je klic.", f.printObj(), 'K');
    }

    @Test
    public void testPlayer07() {
    	//test hledani "teleportu" - test zda lze vstoupit na objekty, ktere to znemoznuji
    	System.out.println("testPlayer07");
    	Table t1 = new Table(5,5);
        t1.insertLine("WWWWW");
        t1.insertLine("W..KW");
        t1.insertLine("WKWGW");
        t1.insertLine("WWFGW");
        t1.insertLine("WWWWW");
        
        
        TableField f = t1.fieldAt(0,0);
        
        //umisten hrac na policku s pohledem doprava!
        Player pl = new Player(1,1, f, 1);
		f.seize(pl);
		assertNotNull("Hrac byl vytvoren.", pl);
        //posune se doprava
		t1.printTable();
    	System.out.println("");
		
		System.out.print("MOVE: ");
        pl.move();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (2)", f.positionCol(), 2);
        assertEquals("Je ve stejnem radku (1)", f.positionRow(), 1);
        //zkousi posun doprava
        assertFalse("Nemuze se posunot doprava-je tam klic.",pl.move());
        
        System.out.print("TAKE: ");
        assertTrue("Vzali jsme klic",pl.takeKey());
        System.out.println("ok key: "+pl.keyCount());
        t1.printTable();
    	System.out.println("");
    	
        f = f.fieldOnPosition(1,3);
    	assertEquals("Na policku 1,3 je je prazdny objekt.", f.printObj(), '.');
    	
    	
        assertTrue("Posunuli jsme se na misto klice",pl.move());
        assertEquals("Je ve stejnem sloupci (3)", f.positionCol(), 3);
        assertEquals("Je ve stejnem radku (1)", f.positionRow(), 1);
        
        System.out.print("RIGHT: ");
        pl.rotateRight();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
        assertFalse("Nemuze na branu ktera je zavrena",pl.move());
        assertTrue("Otevre branu",pl.openGate());
        assertTrue("Posune se na misto prvni brany",pl.move());
        assertFalse("Neotevre branu",pl.openGate());
        assertFalse("Nemuze na branu ktera je zavrena",pl.move());
        
        //vraci se pro klic
        System.out.print("LEFT: ");
        pl.rotateLeft();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
        System.out.print("LEFT: ");
        pl.rotateLeft();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
        assertFalse("Klic uz neexistuje",pl.takeKey());
        System.out.print("MOVE: ");
        pl.move();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
        System.out.print("LEFT: ");
        pl.rotateLeft();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
        System.out.print("MOVE: ");
        pl.move();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
        System.out.print("MOVE: ");
        pl.move();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
        System.out.print("LEFT: ");
        pl.rotateLeft();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
        System.out.print("TAKE: ");
    	assertTrue("Vezme druhy klic",pl.takeKey());
    	System.out.println("ok keys: "+ pl.keyCount());
    	t1.printTable();
    	System.out.println("");
    	
        System.out.print("RIGHT: ");
        pl.rotateRight();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
        System.out.print("RIGHT: ");
        pl.rotateRight();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
    	System.out.print("RIGHT: ");
        pl.rotateRight();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
    	System.out.print("MOVE: ");
        pl.move();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
    	System.out.print("MOVE: ");
        pl.move();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
    	System.out.print("RIGHT: ");
        pl.rotateRight();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
    	System.out.print("MOVE: ");
        pl.move();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
    	
    	System.out.print("OPEN: ");
        assertTrue("Otevre druhou branu.", pl.openGate());
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
        System.out.print("MOVE: ");
        pl.move();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
    	System.out.print("RIGHT: ");
        pl.rotateRight();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
    	
    	System.out.print("MOVE: ");
        pl.move();
        System.out.println("ok");
        t1.printTable();
    	System.out.println("");
    	
    	System.out.print("TAKE: ");
    	assertFalse("Na policku neni klic",pl.takeKey());
    	System.out.println("ok");
    	t1.printTable();
    	System.out.println("");
    	
    	assertTrue("Je v cili!",pl.isWinner());
    	
    }
}
