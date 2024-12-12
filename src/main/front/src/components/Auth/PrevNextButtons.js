import React from 'react';
import { startTransition } from 'react';

const PrevNextButtons = ({ onPrev, onNext, disableNext, showNextButton = true, prevText = "이전" }) => {
  const handlePrev = () => {
    startTransition(() => {
      onPrev();
    });
  };

  const handleNext = () => {
    if (!disableNext) {
      startTransition(() => {
        onNext();
      });
    }
  };

  return (
    <div className="button-container">
      <div>
        <button type="button" className="btn btn_prev" onClick={handlePrev}>
          <span className="button-text">{prevText}</span>
        </button>
      </div>
      {showNextButton && (
        <div>
          <button 
            type="button" 
            className={`btn btn_next ${disableNext ? 'disabled' : ''}`} 
            onClick={handleNext}
            disabled={disableNext}
          >
            <span className="button-text">다음</span>
          </button>
        </div>
      )}
    </div>
  );
};

export default PrevNextButtons;