import React, {useState} from 'react';

import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper,
  Button
} from "@material-ui/core";

import {restCall} from '../utils/RestComponent'


const ListStudents = props => {
    const [apiRet, setApiRet] = useState({});

    const handleChange=(event)=>{
        if(event.target.id==='lastName'){
            restCall('GET', `${props.serverURI}/api/findStudents?lastName=${event.target.value}`, setApiRet, props.token, null);
        }
    }

    return(
        <>
            <div
                style={{
                display: "flex",
                justifyContent: "center",
                margin: 20,
                padding: 20
                }}
            >
               <div style={{width:"80%"}}>
                    <form onChange={handleChange.bind(this)}>
                        <h1>List Students</h1>
                        {apiRet.error !== undefined?(<FormLabel component="legend">{apiRet.error}</FormLabel>):(<></>)}
                        <FormControl margin="normal" fullWidth>
                            <InputLabel htmlFor="lastName">Last Name</InputLabel>
                            <Input id="lastName" type="text"/>
                        </FormControl>
                    </form>
                </div>
            </div>

            <div
                style={{
                display: "flex",
                justifyContent: "center",
                margin: 20,
                padding: 20
                }}
            >
            <TableContainer component={Paper} style={{width:"80%"}}>
                <Table aria-label="simple table">
                <TableHead>
                  <TableRow>
                    <TableCell>First Name</TableCell>
                    <TableCell>Last Name</TableCell>
                    <TableCell>Number</TableCell>
                    <TableCell>Belt</TableCell>
                    <TableCell>Stripes</TableCell>
                    <TableCell>&nbsp;</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                    {apiRet!== undefined
                        && apiRet!==null && apiRet.error === undefined
                        && Object.getOwnPropertyNames(apiRet).length !== 0?(
                        <>
                            {apiRet.map(student=>(
                                <TableRow key='{student.id}'>
                                    <TableCell component="th" scope="row">
                                        {student.firstName}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {student.lastName}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {student.number}
                                    </TableCell>
                                    <TableCell scope="row">

                                    </TableCell>
                                    <TableCell scope="row">
                                        {student.stripes}
                                    </TableCell>
                                    <TableCell scope="row">
                                        <Button onClick={()=>{
                                            const newProps = {...props.globalData};
                                            newProps.appPage='editStudent';
                                            props.setGlobalData(newProps);
                                            }}>Edit</Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </>
                    ):(null)}
                </TableBody>
                </Table>
            </TableContainer>
            </div>
            </>
    );
}

export default ListStudents;