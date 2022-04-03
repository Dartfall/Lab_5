package Model;

import java.util.HashMap;
import java.util.Map;

public class Cell
{
    /*------------------------ Реализация стен (Проверка стен в направлении и установка стен) --------------------------------------------------*/
    //Стены
    private final Map<Direction, Wall> walls = new HashMap<>();

    //Узнать есть ли стена в направлении
    public boolean haveAWall(Direction dir)
    {
	//Если направление не передано, вернуть ложь
        if (dir == null)
            return false;

	//Получить стену в нужном напралении и порвериь её на null
        return walls.get(dir) != null;
    }

    //Установить стену
    void setWall(Wall wall, Direction dir)
    {
	//Если стена в выбранном направлении уже установлена или передано пустое напраление или передана несуществующая стена
        if (walls.putIfAbsent(dir, wall) != null || dir == null || wall == null)
        {
		//Выкинуть исключение неверного аргумента мктода
            throw new IllegalStateException();
        }

	//Установить стену в соседнюю ячейку
        Cell neighbourCell = neighbours.get(dir);

	//Если соседняя ячейка существует
        if (neighbourCell != null)
        {
	    //Если соседняя ячейка не имеет стены в нужном направлении
            if (!neighbourCell.haveAWall(dir.reverse()))
            {
		//Установить в соседней ячейку в нужном направлении
                neighbourCell.setWall(wall, dir.reverse());
            }
        }
    }

    //Закончил комментирование

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
