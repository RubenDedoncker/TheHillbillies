package tests.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import hillbillies.model.Block;
import hillbillies.model.Boulder;
import hillbillies.model.Faction;
import hillbillies.model.Unit;
import hillbillies.model.Vector;
import hillbillies.model.World;
import hillbillies.model.Block.BlockType;
import hillbillies.part2.listener.TerrainChangeListener;

public class BoulderTest {

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
	public void testBoulderPosition(){
		Vector v = new Vector (1, 0, 0);
		Boulder boulder = new Boulder(v.getX(), v.getY(), v.getZ(), (int)30);
		testWorld.addBoulder(boulder);
		assertEquals(v.getCoeff(), boulder.getPosition().getCoeff());
	}
	@Test
	public void testBoulderInWorld(){
		Vector v = new Vector (1, 0, 0);
		Boulder boulder = new Boulder(v.getX(), v.getY(), v.getZ(), (int)30);
		testWorld.addBoulder(boulder);
		assertEquals(1, testWorld.getBoulders().size());
	}
}
