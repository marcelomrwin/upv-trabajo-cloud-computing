import {createContext, useState} from "react";

const ALERT_TIME = 7000;
const initialState = {
    title:'',
    text:'',
    type:'',
};

const AlertContext = createContext({
    ...initialState,
    setAlert: () => {},
});

export const AlertProvider = ({ children }) => {
    const [title, setTitle] = useState('');
    const [text, setText] = useState('');
    const [type, setType] = useState('');

    const setAlert = (title, text, type) => {
        setTitle(title);
        setText(text);
        setType(type);

        setTimeout(() => {
            setTitle('');
            setText('');
            setType('');
        }, ALERT_TIME);
    };

    return (
        <AlertContext.Provider
            value={{
                title,
                text,
                type,
                setAlert,
            }}
        >
            {children}
        </AlertContext.Provider>
    );
};

export default AlertContext;