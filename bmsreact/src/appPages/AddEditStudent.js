import React, {useState, useEffect} from 'react';
import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  Button,
  MenuItem,
  Select
} from "@material-ui/core";
import {restCall} from '../utils/RestComponent'

const AddEditStudent = props => {
    const [apiRet, setApiRet] = useState({});
    const [editState, setEditState] = useState({
        belt:'1',
        stripes:'0'
    });

    useEffect(() => {

        if(Object.getOwnPropertyNames(editState).length < 3 && props.globalData.studentId>0){
            restCall('GET', `${props.globalData.serverURI}/api/getStudents?id=${props.globalData.studentId}`, setApiRet, props.globalData.token, null);
        }else if(Object.getOwnPropertyNames(editState).length === 0){
            setEditState({belt:'1',stripes:'0'});
        }

        if(Object.getOwnPropertyNames(editState).length < 3 && apiRet!==null && apiRet.errors === undefined && apiRet.id!==undefined){
            setEditState(apiRet);
            console.log(apiRet);
            setApiRet(null);
        }
    },[editState, apiRet, props.globalData.serverURI, props.globalData.studentId, props.globalData.token]);

    function changeFunc(event){
        const newState = {...editState};
        if(event.target.id==='number')
            newState.number=event.target.value;
        if(event.target.id==='firstName')
            newState.firstName=event.target.value;
        if(event.target.id==='lastName')
            newState.lastName=event.target.value;

        setEditState(newState);
    }

    function changeBelt(event){
        const newState = {...editState};
        newState.belt=event.target.value;
        setEditState(newState);
    }

    function changeStripes(event){
        const newState = {...editState};
        newState.stripes=event.target.value;
        setEditState(newState);
    }

    const saveStudent = () =>{
        if(Object.getOwnPropertyNames(editState).length !== 0){
            restCall('POST', `${props.globalData.serverURI}/api/saveStudent`, setApiRet, props.globalData.token, editState);
        }
    };

    return(
        <div
            style={{
            display: "flex",
            justifyContent: "center",
            margin: 20,
            padding: 20
            }}
        >
            <form style={{ width: "80%" }}>
                <h1>{props.globalData.studentId===0?(<>Add Student</>):(<>Edit Student</>)}</h1>
                {apiRet!==null && apiRet.error !== undefined?(<FormLabel component="legend">{apiRet.error}</FormLabel>):(<></>)}
                {apiRet!==null && apiRet === 'Update Successful'?(<FormLabel component="legend">Update Successful</FormLabel>):(<></>)}
                {Object.getOwnPropertyNames(editState).length !== 0 ?(<>
                    <InputLabel htmlFor="number">Student Number</InputLabel>
                    <FormControl margin="normal" fullWidth>
                        <Input id="number" type="text" value={editState.number} onChange={changeFunc}/>
                    </FormControl>

                    <InputLabel htmlFor="firstName">First Name</InputLabel>
                    <FormControl margin="normal" fullWidth>
                        <Input id="firstName" type="text" value={editState.firstName} onChange={changeFunc}/>
                    </FormControl>

                    <InputLabel htmlFor="lastName">Last Name</InputLabel>
                    <FormControl margin="normal" fullWidth>
                        <Input id="lastName" type="text" value={editState.lastName} onChange={changeFunc}/>
                    </FormControl>

                    <InputLabel htmlFor="belt">Belt</InputLabel>
                    <FormControl margin="normal" fullWidth>
                        <Select
                          labelId="belt"
                          id="belt"
                          value={editState.belt}
                          onChange={changeBelt}
                        >
                            {props.globalData.commonData.belts.map(belt=>(
                                <MenuItem key={belt.beltId}  value={belt.beltId}>{belt.beltColor}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <InputLabel htmlFor="stripes">Stripes</InputLabel>
                    <FormControl margin="normal" fullWidth>
                        <Select
                          labelId="stripes"
                          id="stripes"
                          value={editState.stripes}
                          onChange={changeStripes}
                        >
                            <MenuItem key='0' value='0'>0</MenuItem>
                            <MenuItem key='1' value='1'>1</MenuItem>
                            <MenuItem key='2' value='2'>2</MenuItem>
                            <MenuItem key='3' value='3'>3</MenuItem>
                            <MenuItem key='4' value='4'>4</MenuItem>
                            <MenuItem key='5' value='5'>5</MenuItem>
                            <MenuItem key='6' value='6'>6</MenuItem>
                        </Select>
                    </FormControl>

                    <Button variant="contained" color="primary" size="medium" onClick={saveStudent}>
                        Send
                    </Button>
                </>):null}
            </form>
        </div>
    );
}

export default AddEditStudent;