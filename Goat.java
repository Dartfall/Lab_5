package Model;

import Model.events.GoatActionEvent;
import Model.events.GoatActionListener;

import java.util.ArrayList;

public class Goat extends Unit {
    //Конструктор
    Goat(EnergyNumber energy)
    {
        if (energy == null)
        {
            throw new IllegalArgumentException();
        }

        energyNumber = energy;
    }

    /* --------------------- Количество оставшихся ходов ----------------------------------- */
    EnergyNumber energyNumber;

    public EnergyNumber getEnergyNumber() {
        return energyNumber;
    }

    /* -------------------- Движение -------------------------*/
    //Движение в заданном направлении
    public void move(Direction dir){
        setGazeDirection(dir);

        if (!currentCell.haveAWall(dir))
        {
            Cell neighbourCell = currentCell.getNeighbour(dir);

            Unit neighbourUnit = neighbourCell.getUnit();

            if ((neighbourUnit instanceof Cabbage) || neighbourUnit == null)
            {
                if (neighbourUnit != null)
                {
                    this.eatCabbage((Cabbage)neighbourUnit);
                }

                currentCell.removeUnit();

                neighbourCell.setStoredUnit(this);

                energyNumber.decrementEnergy();

                fireGoatMoved();
            }
        }
    }

    //Движение ящика от козы
    public void moveBoxAwayFromGoat(Direction dir)
    {
        if (dir == null)
        {
            throw new IllegalArgumentException();
        }

        Unit unit = currentCell.getUnitFromNeighbour(dir);

        if (unit != null && (unit instanceof Box))
        {
            Box box = (Box)unit;

            box.move(dir);

            this.move(dir);
        }
    }

    //Движение ящика к козе
    public void moveBoxTowardGoat(Direction dir)
    {
        if (dir == null)
        {
            throw new IllegalArgumentException();
        }

        Unit unit = currentCell.getUnitFromNeighbour(dir);

        if (unit != null && (unit instanceof Box))
        {
            Box box = (Box)unit;

            Direction reversedDirection = dir.reverse();

            this.move(reversedDirection);

            box.move(reversedDirection);
        }
    }

    /* -------------------- Направление взгляда козы (Установить направление, получить направление) ---------------------------*/
    private Direction gazeDirection = Direction.north();

    //Установить направление взгляда
    private void setGazeDirection(Direction dir)
    {
        if (dir == null)
        {
            throw new IllegalArgumentException();
        }

        gazeDirection = dir;
    }

    //Получить направление взгляда
    public Direction getGazeDirection() {
        return gazeDirection;
    }

    /* ----------------------- Капуста ------------------------------- */
    private boolean isAte = false;

    private void setAte()
    {
        isAte = true;
    }

    private void eatCabbage(Cabbage cabbage)
    {
        if (cabbage == null)
        {
            throw new IllegalArgumentException();
        }

        cabbage.setIsEaten();

        this.setAte();
    }

    public boolean IsAte()
    {
        return isAte;
    }

    /* ----------------- Реализация событий козы --------------------- */
    //Слушатели
    private final ArrayList<GoatActionListener> listeners = new ArrayList<>();

    //Добавить слушателя
    void addListener(GoatActionListener listener)
    {
        if (listener == null)
        {
            throw new IllegalArgumentException();
        }

        listeners.add(listener);
    }

    //Удалить слушателя
    void removeListener(GoatActionListener listener)
    {
        listeners.remove(listener);
    }

    //Испустить событие движения козы
    private void fireGoatMoved()
    {
        GoatActionEvent e = new GoatActionEvent(this);

        for (GoatActionListener listener : listeners)
        {
            listener.goatMoved(e);
        }
    }
}
