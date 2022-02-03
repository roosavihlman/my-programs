import React, {useState} from 'react';
import List from './List';

const Watch = () => {
    //tehdään statet statusValue = tunnisteen status, statusTime = ajan status
    //status = yleinen kellon status (toiminnan kannalta), value = tunniste,
    //interv = intervalli, timer = sekunttikello
    const {addItem, addTime, printList} = List();
    const [statusValue, setStatusValue] = useState(false);
    const [statusTime, setStatusTime] = useState(false);
    const [status, setStatus] = useState(false);
    const [value, setValue] = useState("");
    const [interv, setInterv] = useState();
    const [timer, setTimer] = useState({
        h: 0,
        m: 0,
        s: 0,
        ms: 0,
        
    });
    
    //kellon starttaus, jos status on fales (=kello ei vielä käy), laitetaan kello käyntiin
    //muuttamalla status true:ksi, StatusTime false:ksi, käynnistetään run()-funktiolla, käynnistetään
    //intervalli setInterv:llä ja varmistetaan kaikki tulostamalla console logiin "timer start"
    const start = () => {
        if(status===false){
            setStatus(true);
            setStatusTime(false);
            run();
            setInterv(setInterval(run, 10));
            console.log("timer start");
        }
        else{
            return;
        }
    };

    //pysäytetään kello, asetetaan siis status false:ksi ja putsataan intervalli
    const stop = () => {
        setStatus(false);
        clearInterval(interv);
    };
    
    //resettaus asettaa timerin 0:0:0:0 asentoon, pysäyttää intervallin ja asettaa statuksen falsek:si
    const reset = () => {
        setStatus(false);
        clearInterval(interv);
        setTimer({ms:0, s:0, m:0, h:0})
    };

    //ajan päivitykseen tarvittavat muuttujat
    var updateMs = timer.ms
    var updateS = timer.s
    var updateM = timer.m
    var updateH = timer.h

    //run()-funktio päivittää kelloa
    const run = () => {
        if(updateM === 60){
            updateH++;
            updateM = 0;
        }
        if(updateS === 60){
            updateM++;
            updateS = 0;
        }
        if(updateMs === 100){
            updateS++;
            updateMs = 0;
        }
        updateMs++;
        return setTimer({ms:updateMs, s:updateS, m:updateM, h:updateH});
    };

    const handleElement = (element) => {
        setValue(element.target.value);
    };

    //funktio, jota käytetään ajan tallennuksessa
    const wrapperFunction = (value) => {
        //jos aika on nollassa (0:0:0:0) tai tunniste on tyhjä, asetetaan StatusTime ja StatusValue true:ksi
        if(timer.h === 0 && timer.m === 0 && timer.s === 0 && timer.ms === 0){
            setStatusTime(true);
        }
        else if(value === ""){
            setStatusValue(true);
        }
        //muutoin asetetaan StatusValue ja StatusTime false:ksi, käytetään addItem-funktiota (List-komponentista),
        //asetetaan valuesn arvo tyhjäksi merkkijonoksi (input kenttä), nollataan kello ja käytetään addTime-funktiota
        //(List-komponentti). addItem ja addTime lisäävät siis annetut tunnisteen ja ajan listoihin.
        else{
            setStatusValue(false);
            setStatusTime(false);
            addItem(value);
            setValue("");
            reset();
            addTime(timer.h + ":" + timer.m + ":" + timer.s + ":" + timer.ms);
        }
    };

    return (
        <div className="sides">
            <div className="counting">
                <h2 className="secondWatch">Secondwatch ⏱️</h2>
                {/*sekunttikello*/}
                <div className="timer">
                    <p className="numbers">{timer.h}</p> 
                    <p>:</p> 
                    <p className="numbers">{timer.m}</p>
                    <p>:</p>
                    <p className="numbers">{timer.s}</p>
                    <p>:</p>
                    <p className="numbers">{timer.ms}</p>
                </div>
                <br/>
                {/*napit kellon toimintaan*/}
                <button onClick={start}>Start</button>
                
                <button onClick={stop}>Stop</button>

                <button onClick={reset}>Reset</button>
                <br/>
                {/*jos statusTime = true tai statusValue = true, tulostetaan vastaavat tekstit*/}
                <p className="noValue">{(statusTime && "Start the time") || (statusValue && "Give identifier")}</p>
                
                {/*input kenttä ja arvon käsittely*/}
                <input className="input" placeholder="Save as..." maxLength="20"
                onChange={(element) => handleElement(element)}
                value={value} type="text" required/>

                <br/>
                <br/>
                {/*tallennusnappi, jota painettaessa käytetään wrapperFunction()-funktiota input kentän arvolla*/}
                <button onClick={() => wrapperFunction(value)}>Save time</button>
            </div>
            <div>
            {/*käytetään List-komponentin printList()-funktiota listan tulostukseen*/}
            {printList()}
            </div>
            
        </div>
    );
};

export default Watch;