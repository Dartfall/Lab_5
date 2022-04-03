package Model;

import java.util.HashMap;
import java.util.Map;

public class Cell
{
    /*------------------------ Реализация стен --------------------------------------------------*/
    //Стены
    private final Map<Direction, Wall> walls = new HashMap<>();

    //Узнать есть ли стена в направлении
    public boolean haveAWall(Direction dir)
    {
        if (dir == null)
            return false;

        return walls.get(dir) != null;
    }

    //Установить стену
    void setWall(Wall wall, Direction dir)
    {
        if (walls.putIfAbsent(dir, wall) != null || dir == null || wall == null)
        {
            throw new IllegalStateException();
        }

        Cell neighbourCell = neighbours.get(dir);
        if (neighbourCell != null)
        {
            if (!neighbourCell.haveAWall(dir.reverse()))
            {
                neighbourCell.setWall(wall, dir.reverse());
            }
        }
    }

    /*------------------------ Реализация соседних ячеек --------------------------------------------------*/
    //Соседние Ячейки
    private final Map<Direction, Cell> neighbours = new HashMap<>();

    //Получение Соседа
    public Cell getNeighbour(Direction dir)
    {
        if (dir == null)
            throw new IllegalArgumentException();

        return neighbours.get(dir);
    }

    //Установление соседней Ячейки
    void setNeighbour(Cell neighbour, Direction dir)
    {
        if (neighbours.putIfAbsent(dir, neighbour) != null  || dir == null || neighbour == null)
        {
            throw new IllegalStateException();
        }

        if (neighbour.getNeighbour(dir.reverse()) == null)
        {
            neighbour.setNeighbour(this, dir.reverse());
        }
    }

    /*Владение юнитами*/
    //Владеемый юнит
    private Unit storedUnit = null;

    //Установка юнита
    void setStoredUnit(Unit unit)
    {
        if (storedUnit != null || unit == null)
        {
            throw new IllegalStateException();
        }

        storedUnit = unit;

        unit.putIn(this);
    }

    //Извлечение юнита
    void removeUnit()
    {
        if (storedUnit != null)
        {
            Unit unit = storedUnit;

            storedUnit = null;

            unit.removeFromCell();
        }
    }

    //Получение юнита
    public Unit getUnit()
    {
        return storedUnit;
    }

    //Получение юнита соседней ячейки
    public Unit getUnitFromNeighbour(Direction dir)
    {
        if (!this.haveAWall(dir))
        {
            Cell neighbour = this.getNeighbour(dir);

            if (neighbour != null)
            {
                return neighbour.getUnit();
            }
        }

        return null;
    }

    //Свободна ли ячейка
    public boolean isFree()
    {
        return (storedUnit == null);
    }
}
