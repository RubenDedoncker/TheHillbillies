package tests.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import hillbillies.model.Block;
import hillbillies.model.Boulder;
import hillbillies.model.Faction;
import hillbillies.model.Log;
import hillbillies.model.Unit;
import hillbillies.model.Vector;
import hillbillies.model.World;
import hillbillies.model.Block.BlockType;
import hillbillies.part2.listener.TerrainChangeListener;

public class WorldTest {
	private Unit testUnit;
	private World testWorld;
	@Before
	public void setup(){
		this.testUnit = new Unit("Billie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		new Faction(testUnit);
		testUnit.stopDefault();
		Map<ArrayList<Integer>, Block> testmap = new HashMap<ArrayList<Integer>, Block>();
		for (int i=0; i<50;i++){
			for (int j=0; j<50;j++){
				for (int k=0; k<50;k++){
					ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(i, j, k));
					Vector v = new Vector (i, j, k);
					Block B = new Block(v, BlockType.AIR);
					testmap.put(locationArray, B);
				}
			}
		}
		TerrainChangeListener modelLinstener = null;
		this.testWorld = new World(50, 50, 50, testmap, modelLinstener);
		testWorld.addUnit(testUnit);
	}
	
	@Test
	public void testCollapse(){
		for (int i=1; i<49;i++){
			for (int j=1; j<49;j++){
				for (int k=1; k<49;k++){
					ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(i, j, k));
					testWorld.getBlockAtPos(locationArray).setBlockType(BlockType.ROCK);
				}
			}
		}
		ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(25, 25, 0));
		testWorld.getBlockAtPos(locationArray).setBlockType(BlockType.ROCK);
		int solidCounter = 0;
		for (Map.Entry<ArrayList<Integer>, Block> entry : testWorld.getGameWorld().entrySet())
			if (entry.getValue().isSolid())
				solidCounter++;
		assertEquals(110593, solidCounter);
		testWorld.setToPassable(testWorld.getBlockAtPos(locationArray));
		int solidCounter2 = 0;
		for (Map.Entry<ArrayList<Integer>, Block> entry : testWorld.getGameWorld().entrySet())
			if (entry.getValue().isSolid())
				solidCounter2++;
		assertEquals(0, solidCounter2);
	}
	@Test
	public void testNoCollapse(){
		for (int i=1; i<49;i++){
			for (int j=1; j<49;j++){
				for (int k=1; k<49;k++){
					ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(i, j, k));
					testWorld.getBlockAtPos(locationArray).setBlockType(BlockType.ROCK);
				}
			}
		}
		ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(25, 25, 0));
		testWorld.getBlockAtPos(locationArray).setBlockType(BlockType.ROCK);
		ArrayList<Integer> locationArray2 = new ArrayList<Integer>(Arrays.asList(25, 24, 0));
		testWorld.getBlockAtPos(locationArray2).setBlockType(BlockType.ROCK);
		int solidCounter = 0;
		for (Map.Entry<ArrayList<Integer>, Block> entry : testWorld.getGameWorld().entrySet())
			if (entry.getValue().isSolid())
				solidCounter++;
		assertEquals(110594, solidCounter);
		testWorld.setToPassable(testWorld.getBlockAtPos(locationArray));
		int solidCounter2 = 0;
		for (Map.Entry<ArrayList<Integer>, Block> entry : testWorld.getGameWorld().entrySet())
			if (entry.getValue().isSolid())
				solidCounter2++;
		assertEquals(110593, solidCounter2);
	}
	
	@Test
	public void testBorders(){
		Integer check = 50;
		assertEquals(check, testWorld.getBorders().get(0));
		assertEquals(check, testWorld.getBorders().get(0));
		assertEquals(check, testWorld.getBorders().get(0));
	}
	@Test
	public void testBlockType(){
		ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(25, 25, 0));
		testWorld.getBlockAtPos(locationArray).setBlockType(BlockType.ROCK);
		assertEquals(BlockType.ROCK, testWorld.getBlockAtPos(locationArray).getBlockType());
		ArrayList<Integer> locationArray2 = new ArrayList<Integer>(Arrays.asList(25, 24, 0));
		testWorld.getBlockAtPos(locationArray2).setBlockType(BlockType.WOOD);
		assertEquals(BlockType.WOOD, testWorld.getBlockAtPos(locationArray2).getBlockType());
		ArrayList<Integer> locationArray3 = new ArrayList<Integer>(Arrays.asList(25, 23, 0));
		testWorld.getBlockAtPos(locationArray3).setBlockType(BlockType.WORKSHOP);
		assertEquals(BlockType.WORKSHOP, testWorld.getBlockAtPos(locationArray3).getBlockType());
		ArrayList<Integer> locationArray4 = new ArrayList<Integer>(Arrays.asList(25, 22, 0));
		assertEquals(BlockType.AIR, testWorld.getBlockAtPos(locationArray4).getBlockType());
	}
	@Test
	public void testIsStable(){
		ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(25, 25, 0));
		testWorld.getBlockAtPos(locationArray).setBlockType(BlockType.ROCK);
		ArrayList<Integer> locationArray2 = new ArrayList<Integer>(Arrays.asList(25, 25, 1));
		testWorld.getBlockAtPos(locationArray2).setBlockType(BlockType.ROCK);
		ArrayList<Integer> locationArray3 = new ArrayList<Integer>(Arrays.asList(25, 25, 2));
		testWorld.getBlockAtPos(locationArray3).setBlockType(BlockType.ROCK);
		testWorld.setToPassable(testWorld.getBlockAtPos(locationArray2));
		assertTrue(testWorld.isStable(testWorld.getBlockAtPos(locationArray)));
		assertFalse(testWorld.isStable(testWorld.getBlockAtPos(locationArray3)));
	}
	@Test
	public void testSpawnUnit(){
		testWorld.spawnUnit();
		assertEquals(2, testWorld.getUnits().size());
	}
	@Test
	public void testAddUnit(){
		Unit testUnit2 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		testWorld.addUnit(testUnit2);
		assertEquals(2, testWorld.getUnits().size());
	}
	@Test
	public void testGetUnits(){
		assertTrue(testWorld.getUnits().size()==1);
		assertTrue(testWorld.getUnits().contains(testUnit));
	}
	@Test
	public void testActiveFactions(){
		testWorld.spawnUnit();
		assertEquals(2, testWorld.getFactions().size());
		testWorld.removeFaction(testWorld.getFactions().get(0));
		assertEquals(1, testWorld.getFactions().size());
	}
	@Test
	public void testDirectlyAdjacet(){
		ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(25, 25, 25));
		assertTrue(testWorld.getDirectlyAdjacent(testWorld.getBlockAtPos(locationArray)).size()==6);
		ArrayList<Integer> locationArray2 = new ArrayList<Integer>(Arrays.asList(25, 25, 0));
		assertTrue(testWorld.getDirectlyAdjacent(testWorld.getBlockAtPos(locationArray2)).size()==5);
		ArrayList<Integer> locationArray3 = new ArrayList<Integer>(Arrays.asList(0, 0, 0));
		assertTrue(testWorld.getDirectlyAdjacent(testWorld.getBlockAtPos(locationArray3)).size()==3);
	}
	@Test
	public void testIsWalkable(){
		ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(25, 25, 1));
		assertFalse(testWorld.isWalkable(testWorld.getBlockAtPos(locationArray)));
		ArrayList<Integer> locationArray2 = new ArrayList<Integer>(Arrays.asList(25, 25, 0));
		assertTrue(testWorld.isWalkable(testWorld.getBlockAtPos(locationArray2)));
		testWorld.getBlockAtPos(locationArray2).setBlockType(BlockType.ROCK);
		assertFalse(testWorld.isWalkable(testWorld.getBlockAtPos(locationArray2)));
		assertTrue(testWorld.isWalkable(testWorld.getBlockAtPos(locationArray)));
	}
	@Test
	public void testIsValidPosition(){
		ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(25, 25, 1));
		assertTrue(testWorld.isValidPosition(locationArray));
		ArrayList<Integer> locationArray2 = new ArrayList<Integer>(Arrays.asList(25, 25, -1));
		assertFalse(testWorld.isValidPosition(locationArray2));
	}
	@Test
	public void testGetWorkshop(){
		assertTrue(testWorld.getWorkshops().isEmpty());
	}
	@Test
	public void testBoulders(){
		assertTrue(testWorld.getBoulders().size()==0);
		Boulder boulder = new Boulder(1.5, 0.5, 0.5, 20);
		testWorld.addBoulder(boulder);
		assertTrue(testWorld.getBoulders().size()==1);
		testWorld.removeBoulder(boulder);
		assertTrue(testWorld.getBoulders().size()==0);
	}
	@Test
	public void testLog(){
		assertTrue(testWorld.getLogs().size()==0);
		Log log = new Log(1.5, 0.5, 0.5, 20);
		testWorld.addLog(log);
		assertTrue(testWorld.getLogs().size()==1);
		testWorld.removeLog(log);
		assertTrue(testWorld.getLogs().size()==0);
	}
	
}

