;;;
;;;; Mandelbrot set demo using Scheme (Kawa) and Eclipse (SchemeScript)
;;;
;;
;; @created   "Fri Jul 16 21:52:48 YEKST 2010"
;; @author    "Pavel Samolisov"
;; @copyright "(c) Pavel Samolisov, Chelyabinsk 2010"
;;

(define-namespace GC <org.eclipse.swt.graphics.GC>)

;;;
;;;; Define a few colors
;;;
(define *black* (Color:new #!null 0 0 0))
(define *white* (Color:new #!null 255 255 255))

;; Image size
(define *max-coordinate* 350)

;; Maximum for tested number
(define *max-number* 1.25)

;; Complex zero
(define *complex-zero* (make-rectangular 0 0))

;; Max iterations
(define *max-iterations* 20)

;; Functional composition: aply function f
;; to result of calculing function g
(define (compose f g)
  (lambda (x) (f (g x))))

;; Repeat function aply: aply function 'func' to result of
;; calculing 'func' n times
(define (repeated func n)
  (if (= n 1)
      func
      (compose func (repeated func (- n 1)))))

;; Mandelbrot function.
;; return Z(n+1) by C and Z(n) (n - index)
(define (mandelf c z)
  (+ (* z z) c))

;; This function return #t if Mandelbrot set contains C
;; Mandelbrot set contains C if |Z(25) by C, 0| < 1
(define (mandel? c)
  (let* ((iterations (repeated (lambda (x) (mandelf c x)) *max-iterations*)))
    (< (magnitude (iterations *complex-zero*)) 1.0)))

;; Return iterations count for test if Mandelbrot set contains C
;; Mandelbrot set does not contain c if |Z(n) by C,0| > 1
;; where n - iteration's number
(define (get-iterations-cnt c)
  (define (test zn)
    (> (magnitude zn) 1.0))

  (define (get-iterations-cnt-iter cnt zn max)
    (if (or (> cnt max) (test zn))
        cnt
        (get-iterations-cnt-iter (+ cnt 1) (mandelf c zn) max)))

  (get-iterations-cnt-iter 0 *complex-zero* *max-iterations*))

;; Transform coordinate (should be from interval [min-coord, max-coord])
;; to number (will be from interval [min-value, max-value])
(define (scale min-value max-value min-coord max-coord value)
  (+ (* (/ (- value min-coord) (- max-coord min-coord))
        (- max-value min-value))
     min-value))

;; Draw Mandelbrot set
(define (draw gc)
  ;; Transform x from coordinate ([0, *max-coordinate*])
  ;; to number ([-*max-number*, *max-number*])
  (define (iscale x)
    (scale (- 0 *max-number*) *max-number* 0 (- *max-coordinate* 1) x))

  ;; Return color for point with coordinates (i, j)
  ;; If scaled value for point exists in Mandelbrot set
  ;; will return *blachk* or *white* if else
#|  (define (get-color i j)
    (let ((c (make-rectangular (iscale i) (iscale j))))
      (if (mandel? c)
          *black*
          *white*)))|#

  ;; Return color for point with coordinates (i, j)
  ;; Using n - iterations count for testing if Mandelbrot
  ;; set contains scaled value for (i, j)
  ;; We map n from 0 to *max-iterations* to color from white to black
  (define (get-color i j)
    (define (build-color-by-n n)
      (define (norm-color-item x)
        (cond ((> x 255) 255)
              ((< x 0) 0)
              (else x)))
      (Color:new #!null (- 255 (* n 10)) (- 255 (* n 10)) (- 255 (* n 10))))
    (let ((c (make-rectangular (iscale i) (iscale j))))
      (build-color-by-n (get-iterations-cnt c))))

  ;; Return color for point with coordinates (i, j)
  ;; Using n - iterations count for testing if Mandelbrot
  ;; set contains scaled value for (i, j)
  ;; We map n from 0 to *max-iterations*/2 to color from black to red,
  ;;          from *max-iterations*/2 to *max-iterations* to
  ;;               color from red to white
  #|(define (get-color i j)
    (define (build-color-by-n n)
      (define (norm-color x)
        (cond ((> x 255) 255)
              ((< x 0) 0)
              (else x)))
      (define (build-black-red-color-n n)
        (Color:new #!null
                   (+ (norm-color (* n 15)) 20)
                   (norm-color 0)
                   (norm-color 0)))
      (define (build-white-black-color-n n)
        (Color:new #!null
                   (norm-color (- 275 (* n 15)))
                   (norm-color (- 275 (* n 15)))
                   (norm-color (- 275 (* n 15)))))
      (if (< n (/ *max-iterations* 2))
          (build-black-red-color-n n)
          (build-white-black-color-n n)))
    (let ((c (make-rectangular (iscale i) (iscale j))))
      (build-color-by-n (get-iterations-cnt c))))|#

  ;; Put pixel with color 'color' to (x, y)
  (define (put-pixel x y color)
    (with-fg-color (gc color)
                   (GC:drawPoint gc x y)))

  ;; Draw pixels map
  (define (iter-i i max-i)
    (define (iter-j j max-j)
      ;; draw one pixel
      (put-pixel i j (get-color i j))
      (if (= j max-j) 0 (iter-j (+ 1 j) max-j)))
    (iter-j 0 *max-coordinate*)
    (if (= i max-i) 0 (iter-i (+ 1 i) max-i)))
  (iter-i 0 *max-coordinate*))

;; Make kawa viewer
(define (mandelbrot-viewer parent)
  (canvas
   parent
   on-paint: (lambda (canvas gc x y w h)
               (with-buffer-gc gc (igc 1280 800)
                               (draw igc)))))

;; Add viewer to the Kawa Scratchpad view
(add-scratchpad-view 'Mandelbrot mandelbrot-viewer)