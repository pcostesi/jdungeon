package jdungeon.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import jdungeon.core.world.World;
import jdungeon.parser.CorruptFileException;
import jdungeon.parser.NoMapsInFolderException;
import jdungeon.parser.WorldAssembler;

import org.junit.Test;

public class testParser {

	@Test
	public void testworldCreator() {
		World goodMap = null;
		World badMap1 = null;
		World badMap2players = null;
		World badMap2boss = null;
		World badMap3 = null;
		//A map in good conditions
		try {
			goodMap = WorldAssembler.worldCreator(new File(
					"src\\jdungeon\\tests\\truefile.board"));
		} catch (FileNotFoundException e) {
			fail();
			e.printStackTrace();
		} catch (CorruptFileException e) {
			fail();
			e.printStackTrace();
		}
		assertNotNull(goodMap);
		//A map with random chars inside
		try {
			badMap1 = WorldAssembler.worldCreator(new File(
					"src\\jdungeon\\tests\\brokenfile.board"));
		} catch (FileNotFoundException e) {
			fail();
			e.printStackTrace();
		} catch (CorruptFileException e) {
			assertTrue(true);
			e.printStackTrace();
		}
		assertNull(badMap1);
		//A map with 2 monsters level 3
		try {
			badMap2boss = WorldAssembler.worldCreator(new File(
					"src\\jdungeon\\tests\\brokenfile.board"));
		} catch (FileNotFoundException e) {
			fail();
			e.printStackTrace();
		} catch (CorruptFileException e) {
			assertTrue(true);
			e.printStackTrace();
		}
		assertNull(badMap2boss);
		//A map with 2 players
		try {
			badMap2players = WorldAssembler.worldCreator(new File(
					"src\\jdungeon\\tests\\brokenfile.board"));
		} catch (FileNotFoundException e) {
			fail();
			e.printStackTrace();
		} catch (CorruptFileException e) {
			assertTrue(true);
			e.printStackTrace();
		}
		assertNull(badMap2players);
		//A map with monsters level > 4
		try {
			badMap2players = WorldAssembler.worldCreator(new File(
					"src\\jdungeon\\tests\\brokenFile4LvlMonster.board"));
		} catch (FileNotFoundException e) {
			fail();
			e.printStackTrace();
		} catch (CorruptFileException e) {
			assertTrue(true);
			e.printStackTrace();
		}
		assertNull(badMap2players);
		//A map with several "wall" at the same point
		try {
			badMap2players = WorldAssembler.worldCreator(new File(
					"src\\jdungeon\\tests\\brokenFileOccupiedPoint.board"));
		} catch (FileNotFoundException e) {
			fail();
			e.printStackTrace();
		} catch (CorruptFileException e) {
			assertTrue(true);
			e.printStackTrace();
		}
		assertNull(badMap2players);
		
	}

	@Test
	public void testWorldList() throws NoMapsInFolderException {
		// Correct Settings
		try {
			assertNotNull(WorldAssembler.worldList(new File(
					"src\\jdungeon\\tests\\")));
		} catch (NoMapsInFolderException e) {

		}
		// Bad Directory
		try {
			WorldAssembler.worldList(new File("src\\jdungeon"));
		} catch (NoMapsInFolderException e) {
			assert (true);
		}

		// Directory with .boards (correct until World name at least, may be
		// wrong afterwards)
		try {
			assertNotNull(WorldAssembler.worldList(new File(
					"src\\jdungeon\\tests")));
		} catch (NoMapsInFolderException e) {
			assert (true);
		}
		assertSame(WorldAssembler.worldList(
				new File("src\\jdungeon\\tests")).size(), 6);
	}

}
