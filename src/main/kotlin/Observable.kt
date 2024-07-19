interface Observable {
    fun registerObserver(observer: ShipmentObserver)
    fun removeObserver(observer: ShipmentObserver)
    fun notifyObservers()
}
