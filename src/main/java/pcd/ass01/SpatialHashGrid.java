package pcd.ass01;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpatialHashGrid {

    private final double cellSize;
    private final Map<GridCell, List<Boid>> grid;

    public SpatialHashGrid(double cellSize) {
        this.cellSize = cellSize;
        this.grid = new ConcurrentHashMap<>();
    }

    public SpatialHashGrid(double cellSize, Map<GridCell, List<Boid>> grid) {
        this.cellSize = cellSize;
        this.grid = Map.copyOf(grid);
    }

    public void clear() {
        grid.clear();
    }

    public static SpatialHashGrid copyOf(SpatialHashGrid original){
        return new SpatialHashGrid(original.cellSize, original.grid);
    }

    public void insert(Boid boid) {
        if (boid == null) return;

        GridCell cell = toCell(boid.getPos());
        grid.computeIfAbsent(cell, k -> new CopyOnWriteArrayList<>()).add(boid);
    }

    public List<Boid> getNeighbors(P2d pos, double radius) {
        List<Boid> neighbors = new CopyOnWriteArrayList<>();
        int minX = (int) Math.floor((pos.x() - radius) / cellSize);
        int maxX = (int) Math.floor((pos.x() + radius) / cellSize);
        int minY = (int) Math.floor((pos.y() - radius) / cellSize);
        int maxY = (int) Math.floor((pos.y() + radius) / cellSize);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                GridCell cell = new GridCell(x, y);
                List<Boid> cellBoids = grid.get(cell);
                if (cellBoids != null) {
                    neighbors.addAll(cellBoids);
                }
            }
        }

        return neighbors;
    }

    private GridCell toCell(P2d pos) {
        int x = (int) Math.floor(pos.x() / cellSize);
        int y = (int) Math.floor(pos.y() / cellSize);
        return new GridCell(x, y);
    }

    // Classe interna per rappresentare le celle della griglia
    private static class GridCell {
        int x, y;

        GridCell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GridCell)) return false;
            GridCell other = (GridCell) o;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}