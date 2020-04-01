(ns mazeserver.generator
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [next.jdbc :as jdbc]))
;;used the sample code from learning material
;;Chris code
;;function to make row
(defn make-row [cols]
  (loop [count 0 row []]
    (if (= cols count)row
                      (recur (inc count)
                             (conj row {:north 0 :east 0 :south 0 :west 0})))))

;;function to make grid
(defn make-grid [rows cols]
  (loop [count 0 grid []]
    (if (= rows count)
      grid
      (recur (inc count) (conj grid (make-row cols))))))


;; representing 4 x 4 grid using atom
(def grid (atom (make-grid 4 4)))


;;function to create cell- shows walls and passages
(defn make-a-cell
  ([row] (make-a-cell row 0))
  ([row col]

   ; store size of grid and number of cells
   (let [size (count @grid) cells (count (first @grid))]

     ; above top row return maze.mv.db
     (cond
       (= row size) @grid
       ; top row && last cell do nothing
       (and (= row (- size 1)) (= col (- cells 1))) 0
       ; top row carve east
       (=  row (- size 1)) (do
                             (swap! grid assoc-in [row col :east] 1)
                             (swap! grid assoc-in [row (+ col 1) :west]))
       1))

   ; not top row && last cell carve north
   (= col (- cells 1)) (do
                         (swap! grid assoc-in [row col :north] 1)
                         (swap! grid assoc-in [(+ row 1) col :south] 1))
   ; not top row carve north or east
   :else
   (if (= 0 (rand-int 2))
     (do
       (swap! grid assoc-in [row col :east] 1)
       (swap! grid assoc-in [row (+ col 1) :west] 1))
     (do
       (swap! grid assoc-in [row col :north] 1)
       (swap! grid assoc-in [(+ row 1) col :south] 1)))))


(defn carve-passages
  ([] (carve-passages 0))
  ([row]
   (if (= row (count @grid))
     @grid
     (do
       (dotimes [col (count (first @grid))]
         (make-a-cell row col))
       (carve-passages (inc row))))))


(carve-passages)
