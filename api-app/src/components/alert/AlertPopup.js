// AlertPopup.js
import {Alert, AlertTitle} from '@mui/material';
import useAlert from "./useAlert";

const AlertPopup = () => {
    const { title, text, type } = useAlert();

    if (title && text && type) {
        return (
            <Alert
                severity={type}
                sx={{
                    position: 'absolute',
                    zIndex: 10,
                }}
            >
                <AlertTitle><strong>{title}</strong></AlertTitle>
                {text}
            </Alert>
        );
    } else {
        return <></>;
    }
};

export default AlertPopup;