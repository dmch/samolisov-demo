(define-namespace Text <org.eclipse.swt.widgets.Text>)
(define-namespace Composite <org.eclipse.swt.widgets.Composite>)
(define-namespace SWT <org.eclipse.swt.SWT>)
(define-namespace TextViewer <org.eclipse.jface.text.TextViewer>)
(define-namespace GridData <org.eclipse.swt.layout.GridData>)
(define-namespace GridLayout <org.eclipse.swt.layout.GridLayout>)
(define-namespace KeyEvent <org.eclipse.swt.events.KeyEvent>)
(define-namespace Color <org.eclipse.swt.graphics.Color>)
(define-namespace Document <org.eclipse.jface.text.Document>)
(define-namespace StyledText <org.eclipse.swt.custom.StyledText>)

(define (install-key-listener search-text result-text)
  (Text:addKeyListener
   search-text
   (object (<org.eclipse.swt.events.KeyAdapter>)
     ((keyReleased (event :: <org.eclipse.swt.events.KeyEvent>)) :: <void>
      (when (= 13 (KeyEvent:.keyCode event))
        (run-search (Text:getText search-text) result-text))))))

(define (run-search text result-text)
  (TextViewer:setDocument result-text (Document:new "searching...")))

(add-scratchpad-view
 '|Symbol Search|
 (lambda (parent)
   (let* ((composite (Composite:new parent 0))
          (search-text (Text:new composite (+ (SWT:.BORDER))))
          (result-text (TextViewer:new composite (+ (SWT:.READ_ONLY) (SWT:.MULTI))))
          (layout      (GridLayout:new 1 #t)))
     (Composite:setLayout composite layout)
     (Composite:setBackground composite (Color:new #!null 255 255 255))
     (Text:setLayoutData search-text
                         (GridData:new (+ (GridData:.HORIZONTAL_ALIGN_FILL)
                                          (GridData:.GRAB_HORIZONTAL))))
     (Text:setLayoutData (TextViewer:getTextWidget result-text)
                         (GridData:new (+ (GridData:.HORIZONTAL_ALIGN_FILL)
                                          (GridData:.GRAB_HORIZONTAL)
                                          (GridData:.VERTICAL_ALIGN_FILL)
                                          (GridData:.GRAB_VERTICAL))))
     (Text:setToolTipText search-text "Enter the symbols to search here.")
     (TextViewer:setEditable result-text #f)
     (TextViewer:setDocument result-text (Document:new "No results."))
     (StyledText:setCaret (TextViewer:getTextWidget result-text) #!null)
     (install-key-listener search-text result-text)
     composite)))