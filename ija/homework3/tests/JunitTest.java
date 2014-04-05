/**
 * Testy na funkcnost souboru.
 * @author xjanou14 Martin Janoušek, xfiala46 Marek Fiala
 * @version 1.2
 */
package ija.homework3.tests;

import static org.junit.Assert.*;
import ija.homework3.io.FileReader;
//import ija.homework3.io.Console;
import ija.homework3.objects.*;
import ija.homework3.table.*;
import ija.homework3.player.*;


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
        Table t1 = new Table(5,5);
        t1.insertLine("WWWWW");
        t1.insertLine("W.F.W");
        t1.insertLine("W.W.W");
        t1.insertLine("W.G.W");
        t1.insertLine("WWWWW");
        
        char sight, before;
        Player h1;
        TableField f1;
        
        h1 = t1.createPlayer();
        assertNotNull("Hlava 1 vytvorena.", h1);
        f1 = h1.seizedField();
        assertNotNull("Hlava ma ulozene pole.", f1);
        
        before = sight = h1.symbolSight();
        h1.rotateLeft();
        assertFalse("Hrac se otocil doleva.", sight == h1.symbolSight());
        sight = h1.symbolSight();
        h1.rotateLeft();
        assertFalse("Hrac se otocil podruhe doleva.", sight == h1.symbolSight());
        
        sight = h1.symbolSight();
        h1.rotateRight();
        assertFalse("Hrac se otocil doprava.", sight == h1.symbolSight());
        sight = h1.symbolSight();
        h1.rotateRight();
        assertFalse("Hrac se otocil podruhe doprava.", sight == h1.symbolSight());
        assertEquals("Hrac se diva do puvodniho smeru.", before, h1.symbolSight());
    
    }

    /**
     * test provadejici primitivni pruchod hrace polem. Testuje se otaceni, posun hrace a zda
     * lze otevrit branu.
     */
    @Test
    public void testPlayer04() {
    	//test policek a rotace a posunu hrace
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
        pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (1)", f.positionCol(), 1);
        assertEquals("Je ve stejnem radku (2)", f.positionRow(), 2);
        
        //hrac se posune dolu
        pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (1)", f.positionCol(), 1);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        
        //hrac se otoci a posune doprava
        pl.rotateLeft();
        pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (2)", f.positionCol(), 2);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        
        //hrac zkousi otevrit branu
        pl.rotateLeft();
        assertFalse("Nemuze vejit do brany", pl.move());
        assertEquals("Je ve stejnem sloupci (2)", f.positionCol(), 2);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        
        //hraci je pridelen klic a otevre branu
        pl.addKeys(1);
        assertTrue("Otevrela se brana",pl.openGate());
        assertTrue("Vejde do brany", pl.move());
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
        //posune se dolu
		pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (1)", f.positionCol(), 1);
        assertEquals("Je ve stejnem radku (2)", f.positionRow(), 2);
        //posune se dolu
        pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (1)", f.positionCol(), 1);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        
        //otoci a posune se doprava
        pl.rotateLeft();
        pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (2)", f.positionCol(), 2);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        
        assertFalse("Nemuze obsadit klic", pl.move());
        assertEquals("Je ve stejnem sloupci (2)", f.positionCol(), 2);
        assertEquals("Je ve stejnem radku (3)", f.positionRow(), 3);
        
        //sebere klic
        assertTrue("Vzali jsme klic",pl.takeKey());
        pl.move();
        pl.rotateLeft();
        //otevre branu
        assertTrue("Otevrela se brana",pl.openGate());
        assertTrue("Vejde do brany", pl.move());
        f = pl.seizedField();
        //vstoupi do otevrene brany
        assertEquals("Je ve stejnem sloupci (3)", f.positionCol(), 3);
        assertEquals("Je ve stejnem radku (2)", f.positionRow(), 2);
        
        assertFalse("Neni vitez!", pl.isWinner());
        //zvitezi
        assertTrue("Vejde na finish.", pl.move());
        assertTrue("Je vitez!", pl.isWinner());
        
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (3)", f.positionCol(), 3);
        assertEquals("Je ve stejnem radku (1)", f.positionRow(), 1);
        
        
    }
    
    @Test
    public void testFileReader(){
    	//Test nacitani souboru a tisknuti spravnych znaku zastupujici objekty v bludisti
    	FileReader fr = new FileReader();
    	assertNotNull("fr neni nullovy", fr);
    	Table t;
    	
    	assertNull("Neexistujici soubor.", fr.openFile("/zadny/soubor.txt"));
    	assertNotNull("Existujici soubor.", t = fr.openFile("maze2"));
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
		pl.move();
        f = pl.seizedField();
        assertEquals("Je ve stejnem sloupci (2)", f.positionCol(), 2);
        assertEquals("Je ve stejnem radku (1)", f.positionRow(), 1);
        //zkousi posun doprava
        assertFalse("Nemuze se posunot doprava-je tam klic.",pl.move());
        
        assertTrue("Vzali jsme klic",pl.takeKey());
        
        f = f.fieldOnPosition(1,3);
    	assertEquals("Na policku 1,3 je je prazdny objekt.", f.printObj(), '.');
    	
        assertTrue("Posunuli jsme se na misto klice",pl.move());
        assertEquals("Je ve stejnem sloupci (3)", f.positionCol(), 3);
        assertEquals("Je ve stejnem radku (1)", f.positionRow(), 1);
        
        pl.rotateRight();
        
        assertFalse("Nemuze na branu ktera je zavrena",pl.move());
        assertTrue("Otevre branu",pl.openGate());
        assertTrue("Posune se na misto prvni brany",pl.move());
        assertFalse("Neotevre branu",pl.openGate());
        assertFalse("Nemuze na branu ktera je zavrena",pl.move());
        
        //vraci se pro klic
        pl.rotateLeft();
        pl.rotateLeft();
        assertFalse("Klic uz neexistuje",pl.takeKey());
        pl.move();
        pl.rotateLeft();
        pl.move();
        pl.move();
        pl.rotateLeft();
        assertTrue("Vezme druhy klic",pl.takeKey());
        pl.rotateRight();
        pl.rotateRight();
        pl.rotateRight();
        pl.move();
        pl.move();
        pl.rotateRight();
        pl.move();
        assertTrue("Otevre druhou branu.", pl.openGate());
        pl.move();
        pl.rotateRight();
        pl.move();
        assertFalse("Na policku neni klic",pl.takeKey());
        assertTrue("Je v cili!",pl.isWinner());
    }
}
