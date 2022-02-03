//Tallennettujen aikojen listaus

import React, {useState} from 'react';
import './styles.css';

const List  = () => {
    //statet items = tunniste, times = tallennettu aika
    const [items, setItems] = useState([]);
    const [times, setTimes] = useState([]);

    //lisätään tunnistelistaan uusi tunniste, mikäli ei ole tyhjä merkkijono
    const addItem = (name) => {
        if(name === ""){
            return;
        }
        setItems([...items, name]);
    };

    //lisätään aika aikojen listaan
    const addTime = (time) => {
        setTimes([...times, time]);
    };

    //remove tyhjentää listat
    const remove = () => {
        setItems([]);
        setTimes([]);
    };

    //printList tulostaa listat
    const printList = () => {
        return (
            <div>
                <h2 className="savedHeader">⛄ Saved times:</h2>
                    {/*mikäli molemmat listat tyhjiä, tulostetaan sitä indikoiva teksti*/}
                    {items.length === 0 && times.length === 0 && <p className="noTimes">No times saved</p>}
            
                <div className="listItems">
                    {/*tunnisteiden listaus*/}
                    <ol className="listName"> {items.map((item,index) => {
                        return <li key={index}>{item}</li>
                        })}
                    </ol>
                    
                    {/*aikojen listaus*/}
                    <ul className="listTime">{times.map((item, index) => {
                        return <ul key={index}>{item}</ul>
                        })}
                    </ul>
                </div>
                
                {/*nappi, jota painettaessa molemmat listat tyhjenevät*/}
                <button onClick={() => remove()}>Clear List</button>
            </div>
        );
    };
    
    //palautetaan metodeja
    return {
        addItem,
        addTime,
        remove,
        printList
    };
    
};

export default List;